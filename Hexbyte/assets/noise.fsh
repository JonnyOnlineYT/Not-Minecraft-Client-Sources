#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 resolution;
uniform float time;
uniform vec2 mouse;

const int   complexity      = 60;                  // More points of color.
const float fluid_speed     = 5.0;                 // Drives speed, higher number will make it slower.
const float color_intensity = 0.2;
uniform sampler2D sTexture;

void main()
{

	vec2 p=(2.0*gl_FragCoord.xy-resolution)/max(resolution.x,resolution.y);
	for(int i=1;i<complexity;i++) {
		vec2 newp=p;
		newp.x+=0.5/float(i)*sin(float(i)*p.y+time/fluid_speed+0.9*float(i))+100.0;
		newp.y+=0.5/float(i)*sin(float(i)*p.x+time/fluid_speed+0.5*float(i+10))-100.0;
		p=newp;
	}

	vec3 col=vec3(color_intensity*sin(4.0*p.x)+color_intensity + 0.2,color_intensity*sin(3.0*p.y)+color_intensity + 0.2,sin(p.x+p.y) + 2.);

	gl_FragColor=vec4(col, 1.0);
	gl_FragColor = vec4(gl_FragColor.xyz, 1.);
}