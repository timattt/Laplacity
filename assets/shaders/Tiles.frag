#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
varying vec2 pos;
uniform float time;

float rand(float p) {
    return fract(sin(p * 12.345) * 42123.45);
}

void main() {
       gl_FragColor = texture2D(u_texture, v_texCoords);
       if (gl_FragColor.r > 0.7 && gl_FragColor.a > 0.8) {
        	float k = 10.;
        	float omega = 10.;
      
        	
        	float coef = (1. + sin(k * (pos.x + pos.y) + omega * time)) / 2.;
        	gl_FragColor.r = 0.8 + coef * 0.2;
       }
        
}