
attribute vec3 a_position;

uniform mat4 u_projTrans;

varying float val;

void main() {
	gl_Position =  u_projTrans * vec4(a_position.xy, -1, 1);
	val = a_position.z;
}