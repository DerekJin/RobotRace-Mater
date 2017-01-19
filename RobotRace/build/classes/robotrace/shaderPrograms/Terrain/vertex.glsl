// simple vertex shader
#version 120
varying vec3 p;

void main()
{
    float x = gl_Vertex.x;
    float y = gl_Vertex.y;
    float height = 0.6 * cos(0.3 * x + 0.2 * y) + 0.4 * cos(x - 0.5 * y);
    
    p = vec3(x,y,height);
    
    gl_Position    = gl_ModelViewProjectionMatrix * vec4(p, 1);      // model view transform

}