#ifdef GL_ES
	precision mediump float;
#endif

varying float val;

int cond(float from, float r, float g, float b, float a) {
	if (val > from) {
		gl_FragColor = vec4(r/255.0, g/255.0, b/255.0, a);
		return 1;	
	}
	return 0;
}

void main() {
	if (cond(0.9, 128., 0., 128.0, 0.3) == 1) {
		return;
	}
	if (cond(0.8, 216., 191., 216.0, 0.3) == 1) {
		return;
	}
	if (cond(0.7, 123., 104., 238.0, 0.3) == 1) {
		return;
	}
	if (cond(0.6, 147., 112., 219.0, 0.3) == 1) {
		return;
	}
	if (cond(0.5, 106., 90., 205.0, 0.3) == 1) {
		return;
	}
	if (cond(0.4, 230., 320., 250.0, 0.3) == 1) {
		return;
	}
	if (cond(0.3, 230., 168., 215.0, 0.3) == 1) {
		return;
	}
	if (cond(0.2, 204., 204., 255.0, 0.3) == 1) {
		return;
	}
	if (cond(0.03, 230., 230., 250.0, 0.3) == 1) {
		return;
	}

	gl_FragColor = vec4(0, 0, 0, 0);
}