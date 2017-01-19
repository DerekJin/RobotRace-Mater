#version 120

uniform sampler2D baseImage;
void main()
{
    gl_FragColor = texture2D( baseImage, gl_TexCoord[0].xy);
}
