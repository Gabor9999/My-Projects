#version 130

// bemeneti változó - most a vertex shader-ből (vagyis ottani out)
in vec4 vs_out_col;
in vec2 vs_out_normCoords;
in vec2 szogek;
const float PI = 3.14159265359;
const float angle_degree = 30.0;
const float radian = radians(angle_degree);

// kimeneti változó - a fragment színe
out vec4 fs_out_col;

float myFunc(const float t) {
	return abs((0.5*cos(2*PI*t)) + 0.5);
}

vec4 colorize(float between0and1) {
	return mix(vec4(0.5f,0.6f,0.7f,1.0f),vec4(0.7f,0.6f,0.5f,1.0f),between0and1);
}

void main()
{
	//				  R, G, B, A
	//fs_out_col = vs_out_col;
	//fs_out_col = vec4(vs_out_normCoords,0.0,1.0);
	//fs_out_col = vec4(myFunc(vs_out_normCoords.x),myFunc(vs_out_normCoords.x), myFunc(vs_out_normCoords.x), 1.0);
	vec2 dir = vec2(cos(radian),sin(radian));
	fs_out_col = vec4(vec3(myFunc(dot(vs_out_normCoords,dir))),1.0);
	//fs_out_col = colorize(myFunc(vs_out_normCoords.x));

	//				  R, G, B, A
	//fs_out_col = vec4(1, 1, 1, 1);
}


// 1. feladat: rajzoljuk ki fehérrel a téglalapot!

// 2. feladat: rajzoljuk ki az origó középpontú, 1 sugarú kört! Mit kell tenni, ha nem a
//    körlapot, hanem csak a körvonalat akarjuk? Eml.: discard eldobja a fragmentet

// 3. feladat: uniform változók - animált kör sugár!

// 4. feladat: komplex számok....

Házi Feladat:
Mandelbrot halmaz: Z0 = 0 -> Zn+1 = Zn^2 + (endl -> konvergens)
myFunc(x:R,y:I) ahol c a bemenet és hívjuk meg 100szor (for ciklus) Z100 < 2.0, ha igaz része a halmaznak ha nem akkor nems
