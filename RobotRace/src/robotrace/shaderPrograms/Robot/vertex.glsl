// simple vertex shader

#version 120

varying vec3 N, P;

void main() {
    
    N = vec3(normalize(gl_NormalMatrix * gl_Normal));

    vec4 P4 = gl_ModelViewMatrix * gl_Vertex;
    P = vec3(P4.xyz / P4.w);

    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_TexCoord[0] = gl_MultiTexCoord0;
}