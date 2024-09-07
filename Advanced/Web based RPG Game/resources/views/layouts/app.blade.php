<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- CSRF Token -->
    <meta name="csrf-token" content="{{ csrf_token() }}">

    <!-- A játék címe -->
    <title>
        Save The Village
    </title>

    @vite('resources/css/menu.css')
</head>
<body>
    <div class="app">
        <div class="nav-wrap">
                <!-- Left Side Of Navbar -->
                <ul class="navbar-l">
                    <div class="img-container">

                    </div>
                </ul>
                <!-- Right Side Of Navbar -->
                <ul class="navbar-r">
                    <!-- Authentication Links -->
                    @guest
                        @if (Route::has('login'))
                            <a class="nav-link" href="{{ route('login') }}">{{ __('Login') }}</a>
                        @endif
                        @if (Route::has('register'))
                            <a class="nav-link" href="{{ route('register') }}">{{ __('Register') }}</a>
                        @endif
                    @else
                        
                    @endguest
                </ul>
            </div>
        <main class="py-4">
            @yield('content')
        </main>

        <!--@yield('scripts')-->
    </div>
</body>
</html>
