<?php

use Illuminate\Support\Facades\Route;
use Inertia\Inertia;
use App\Models\Player;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "web" middleware group. Make something great!
|
*/
Route::group(['middleware' => ['auth']], function() {
    // Home/Main Page
    Route::get('/', function () {
        return Inertia::render('Home');
    });
    // Continue
    Route::get('/Game', function () {
        return Inertia::render('Game', ['started' => 1]);
    });
    // NewGame
    Route::get('/GameNew', function () {
        return Inertia::render('Game', ['started' => 0]);
    });
    // Auto Logout
    Route::get('/logout', function () {
        return view('logout');
    });
    // CSRF-Token
    Route::get('/csrf-token', function() {
        return response()->json(['csrfToken' => csrf_token()]);
    });
    // Get User Data from database
    Route::get('/api/user_data', function () {
        // Check if user is auth
        $user = Auth::user();
        if (!$user) {
            abort(401);
        }
    
        // Retrieve the data passed to the client
        $data = [
            'difficulty' => $user->difficulty,
            'text_size' => $user->text_size,
        ];
    
        return response()->json($data);
    });
    // Get Player Data from database
    Route::get('/api/player_data', function () {    
        $userId = Auth::id();

        // Retrieve player's data from the authenticated user
        $playerData = Player::where('user_id', $userId)->first();
    
        if (!$playerData) {
            // No player data found
            return abort(404, 'Player data not found');
        }
    
        // Data passed
        $data = [
            'chapter' => $playerData->chapter,
            'position_x' => $playerData->position_x,
            'position_y' => $playerData->position_y,
            'potions' => $playerData->potions,
            'main_items' => $playerData->main_items,
            'items' => $playerData->items,
            'consumables' => $playerData->consumables,
            'exp' => $playerData->exp,
            'HP' => $playerData->HP,
            'choices' => $playerData->choices,
            'storyProgress' => $playerData->storyProgress,
            'main_quest' => $playerData->main_quest,
            'side_quest' => $playerData->side_quest,
            'objective' => $playerData->objective,
            'helper' => $playerData->helper,
            'usedCraft' => $playerData->usedCraft,
            'chests' => $playerData->chests,
            'AIdata' => $playerData->AIdata,
        ];
    
        return response()->json($data);
    });

    Route::post('/save-options', [App\Http\Controllers\optionscontroller::class, 'insertdata']);
    Route::post('/save', [App\Http\Controllers\savecontroller::class, 'insertdata']);
});

Auth::routes();