#pragma once

// GLEW
#include <GL/glew.h>

// SDL
#include <SDL.h>
#include <SDL_opengl.h>

class CMyApp
{
public:

	float MouseX;
	float MouseY;
	bool CtrlKeyDown;

	CMyApp(void);
	~CMyApp(void);

	bool Init();
	void Clean();

	void Update();
	void Render();

	void KeyboardDown(SDL_KeyboardEvent&);
	void KeyboardUp(SDL_KeyboardEvent&);
	void MouseDown(SDL_MouseButtonEvent&);
	void MouseUp(SDL_MouseButtonEvent&);
	void MouseMove(SDL_MouseMotionEvent&);
	void MouseWheel(SDL_MouseWheelEvent&);
	void Resize(int, int);
};

