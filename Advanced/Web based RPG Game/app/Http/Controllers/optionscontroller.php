<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Auth;
use App\Session;
use App\Models\User;

class optionscontroller extends Controller
{
    public function insertdata(Request $request){
        $validatedData = $request->validate([
            'difficulty' => 'required|integer',
            'text_size' => 'required|integer',
        ]);

        $id = Auth::id();
    
        // Find the user by ID
        $user = User::find($id);

        // Update the user's options
        if ($user) {
            $user->difficulty = $validatedData['difficulty'];
            $user->text_size = $validatedData['text_size'];

            $user->save();

            return response()->json(['message' => 'Options updated successfully']);
        } else {
            return response()->json(['error' => 'User not authenticated'], 401);
        }
    }
}
