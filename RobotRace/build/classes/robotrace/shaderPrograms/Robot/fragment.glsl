//uniform bool ambient, diffuse, specular;
#version 120
varying vec3 N, P;

vec4 shading(vec3 P, vec3 N, gl_MaterialParameters mat) {

    gl_LightSourceParameters light = gl_LightSource[0];
    vec4 result = vec4(0.0, 0.0, 0.0, 1.0); // opaque black
    //vec4 result = mat.diffuse;
    //gl_LightSourceParameters light = gl_LightSource[0];
    //ambient
    result += mat.ambient * light.ambient; // add ambient color to sum

    vec3 L = normalize(light.position.xyz); // vector towards the light
    float cosTheta = max(0.0, dot(N, L)); // angle between the normal and the light source
    //diffuse
    result += mat.diffuse * light.diffuse * cosTheta; // sum the color of diffuse times the intensity

    //specular
    vec3 E = vec3(0.0); // position of camera in View space
    vec3 V = normalize(E - P); // direction towards viewer
    vec3 iL = normalize(-1.0 * L); // vector comming from light source
    vec3 reflection = normalize(iL - 2.0 * dot(iL, N) * N); 
    float alpha = max(0.0, dot(reflection, V)); 
    result += mat.specular * light.specular * pow(alpha, mat.shininess);

    return result;
}

void main()
{
	gl_MaterialParameters mat = gl_FrontMaterial;
	gl_FragColor = shading(P, N, mat);
}