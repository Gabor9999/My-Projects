<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Auth;
use App\Session;
use App\Models\Player;

class savecontroller extends Controller
{
    public function insertdata(Request $request){
        $validatedData = $request->validate([
            'chapter' => 'required|integer',
            'pos_x' => 'required|integer',
            'pos_y' => 'required|integer',
            'item' => 'required|integer',
            'chests' => 'required|array',
            'AIdata' => 'array',
            'consumables' => 'array',
            'itemIndex' => 'integer',
            'helper' => 'integer',
            'charHP' => 'integer',
            'exp' => 'integer',
            'choices' => 'integer',
            'usedCraft' => 'integer',
            'main_q' => 'integer',
            'obj' => 'integer',
        ]);

        $id = Auth::id();
    
        // Find the user by ID
        $player = Player::find($id);

        if ($player) {
            $main = json_encode($request -> main_items);
            $it = json_encode($request -> items);
            $side_quest = ($request -> side_quests == NULL) ? NULL : json_encode(array("side_quests" => $request -> side_quests));
            $chests = json_encode(array("chests" => $validatedData['chests']));
            $AIdata = json_encode(array("AIdata" => $validatedData['AIdata']));
            $consumables = json_encode(array("consumables" => $validatedData['consumables']));

            $player->update(['chapter' => $validatedData['chapter'],'position_x' => $validatedData['pos_x'],
            'position_y' => $validatedData['pos_y'],'potions' => $validatedData['item'], 'storyProgress' => $validatedData['itemIndex'], 'HP' => $validatedData['charHP'],'choices' => $validatedData['choices'],
            'helper' => $validatedData['helper'], 'objective' => $validatedData['obj'], 'main_quest' => $validatedData['main_q'],'exp' => $validatedData['exp'],'usedCraft' => $validatedData['usedCraft'],
            "main_items" => $main, "items" => $it, 'side_quest' => $side_quest, 'chests' => $chests, 'consumables' => $consumables, 'AIdata' => $AIdata
            ]);
            return response()->json(['message' => 'Player updated successfully']);
        } else {
            return response()->json(['error' => 'Player not authenticated'], 401);
        }
    }
}