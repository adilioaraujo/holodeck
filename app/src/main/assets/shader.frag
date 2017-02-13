#extension GL_OES_EGL_image_external : require
precision mediump float;

uniform int uMaterial;
uniform samplerExternalOES uTexture1;
uniform samplerCube uTexture2;

varying vec4 vPosition;
varying vec4 vColor;
varying vec2 vTexture;

void main() {

    if (uMaterial == 1) {
        vec4 color = vColor;
        color *= texture2D(uTexture1, vTexture);
        color.rgb /= color.a;
        gl_FragColor = color;

    } else if (uMaterial == 2) {
        vec4 color = vColor;
        color *= textureCube(uTexture2, vPosition.xyz);
        gl_FragColor = color;

    } else {
        gl_FragColor = vColor;
    }

}