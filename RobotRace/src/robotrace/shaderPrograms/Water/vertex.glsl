// simple vertex shader
#version 120

uniform float tAnim;

void main()
{
    float x = gl_Vertex.x;
    float y =  gl_Vertex.y;

    float height = 0.6 * cos(0.3  * x +  0.2 * y + tAnim)+ 0.4 * cos(x - 0.5 * y);

    gl_Position = gl_ModelViewProjectionMatrix * vec4(x, y, height, 1);
    gl_FrontColor = gl_Color;
}
