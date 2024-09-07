@extends('layouts.app')

@vite('resources/css/menu.css')

@section('content')
    <div class="center-card">
        <div class="card">
            <div class="card-header">Please login to play!</div>

            <div class="card-body">
                <form method="POST" action="{{ route('login') }}">
                    @csrf

                    <div class="row">
                        <label for="email" class="col-md-4 col-form-label text-md-end">{{ __('Email Address') }}</label>
                        <div class="col-md-6">
                            <input id="email" type="email" class="form-control @error('email') is-invalid @enderror" name="email" value="{{ old('email') }}" style="width: 100%" autofocus required autocomplete="on">

                            @error('email')
                                <div class="invalid-feedback" role="alert">
                                    <strong>{{ $message }}</strong>
                                </div>
                            @enderror
                        </div>
                    </div>

                    <div class="row">
                        <label for="password" class="col-md-4">{{ __('Password') }}</label>
                        <div class="col-md">
                            <input id="password" type="password" class="form-control @error('password') is-invalid @enderror" name="password" style="width: 100%" required autocomplete="on">

                            @error('password')
                                <div class="invalid-feedback" role="alert">
                                    <strong>{{ $message }}</strong>
                                </div>
                            @enderror
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-check">
                            <input class="form-checkbox" type="checkbox" name="remember" id="remember" {{ old('remember') ? 'checked' : '' }}>

                            <label class="remember-me" for="remember">
                                {{ __('Remember Me') }}
                            </label>
                        </div>
                    </div>

                    <div class="lgn">
                        <button type="submit" class="btn-primary">
                            {{ __('Login') }}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
@endsection
