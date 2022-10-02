#ifdef GL_ES
	precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform int token;

vec3 getColor() {
	int v = int(float(token) - (5.0 * floor(float(token)/5.0)));
	vec3 res = vec3(0, 0, 0);
	
	if (v == 0) res = vec3(177, 184, 220);
	if (v == 1) res = vec3(195, 195, 195);
	if (v == 2) res = vec3(220, 177, 198);
	if (v == 3) res = vec3(193, 177, 220);
	if (v == 4) res = vec3(190, 220, 200);
	
	res *= (1.0 / 255.0);
	
	return res;
}

void main() {
	vec4 res = texture2D(u_texture, v_texCoords);
	float val = (res.x + res.y + res.z) / 3.0;
	if (res.w > 0.01) {
		res = vec4(getColor() * val, 0.6);
	} else {
		res = vec4(0, 0, 0, 0);
	}
	gl_FragColor = res;
}