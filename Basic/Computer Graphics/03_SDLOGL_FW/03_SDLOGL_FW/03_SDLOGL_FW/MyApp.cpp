#include "MyApp.h"
#include <math.h>

CMyApp::CMyApp(void)
{
	MouseX = 0.0;
	MouseY = 0.0;
	CtrlKeyDown = false;
}


CMyApp::~CMyApp(void)
{
}

bool CMyApp::Init()
{
	// törlési szín legyen kékes
	glClearColor(0.125f, 0.25f, 0.5f, 1.0f);

	// kapcsoljuk be a hatrafele nezo lapok eldobasat
	glEnable(GL_CULL_FACE);
	glEnable(GL_DEPTH_TEST);

	return true;
}

void CMyApp::Clean()
{
}

void CMyApp::Update()
{
	GLint m_viewport[4];
	glGetIntegerv(GL_VIEWPORT, m_viewport);
	glClearColor(MouseX / m_viewport[2], MouseY / m_viewport[3], (1.0f-MouseX-MouseY), 1.0f);
}


void CMyApp::Render()
{
	// töröljük a frampuffert (GL_COLOR_BUFFER_BIT) és a mélységi Z puffert (GL_DEPTH_BUFFER_BIT)
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
}

void CMyApp::KeyboardDown(SDL_KeyboardEvent& key)
{
	if (key.keysym.mod & KMOD_CTRL) {
		CtrlKeyDown = true;
	}
}

void CMyApp::KeyboardUp(SDL_KeyboardEvent& key)
{
}

void CMyApp::MouseMove(SDL_MouseMotionEvent& mouse)
{
	if (CtrlKeyDown == true) {
		MouseX = (float)mouse.x;
		MouseY = (float)mouse.y;
		CtrlKeyDown = false;
	}
}

void CMyApp::MouseDown(SDL_MouseButtonEvent& mouse)
{
}

void CMyApp::MouseUp(SDL_MouseButtonEvent& mouse)
{
}

void CMyApp::MouseWheel(SDL_MouseWheelEvent& wheel)
{
}

// a két paraméterben az új ablakméret szélessége (_w) és magassága (_h) található
void CMyApp::Resize(int _w, int _h)
{
	glViewport(0, 0, _w, _h );
}