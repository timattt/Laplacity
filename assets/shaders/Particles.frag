#ifdef GL_ES
	precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform vec3 color;

void main() {
	vec4 res = v_color * texture2D(u_texture, v_texCoords);
	float val = (res.x + res.y + res.z) / 3.0;
	if (res.w > 0.01) {
		res = vec4(color * val, 0.6);
	} else {
		res = vec4(0, 0, 0, 0);
	}
	gl_FragColor = res;
}