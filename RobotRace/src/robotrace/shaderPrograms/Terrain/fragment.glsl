#version 120
varying vec3 p;

void main()
{

    if (p.z < 0.0)
    {
        gl_FragColor = vec4(0.275f, 0.510f, 0.706f, 1);
    }

    else if (p.z < 0.5)
    {
        gl_FragColor = vec4(0.824f, 0.706f, 0.549f, 1);
    }

    else
    {
        gl_FragColor = vec4(0.133f, 0.545f, 0.133f, 1);
    }
}
