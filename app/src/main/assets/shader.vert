uniform mat4 uMV;
uniform mat4 uMVP;
uniform vec3 uLight;
uniform int uMaterial;

attribute vec4 aPosition;
attribute vec4 aColor;
attribute vec3 aNormal;
attribute vec2 aTexture;

varying vec4 vPosition;
varying vec4 vColor;
varying vec2 vTexture;

void main() {

    if (uMaterial == 0 || uMaterial == 1) {
        vec3 modelViewVertex = vec3(uMV * aPosition);
        vec3 modelViewNormal = vec3(uMV * vec4(aNormal, 0.0));
        float distance = length(uLight - modelViewVertex);
        vec3 lightVector = normalize(uLight - modelViewVertex);
        float diffuse = max(dot(modelViewNormal, lightVector), 0.5);
        diffuse = diffuse * (1.0 / (1.0 + (0.00001 * distance * distance)));
        vColor = vec4(aColor.rgb * diffuse, aColor.a);
    } else {
        vColor = aColor;
    }

    vPosition = aPosition;
    vTexture = aTexture;

    gl_Position = uMVP * aPosition;
}