#ifdef GL_ES
	precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform vec3 color;

void main() {
	gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
	float val = (gl_FragColor.x + gl_FragColor.y + gl_FragColor.z) / 3.0;
	if (gl_FragColor.w > 0.01) {
		gl_FragColor = vec4(color.xyz * val, min(gl_FragColor.w * 2, 1));
	} else {
		gl_FragColor = vec4(0, 0, 0, 0);
	}
}