<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;

class Player extends Model
{
    use HasFactory;
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'chapter', 'position_x', 'position_y', 'potions', 'main_items', 'items','consumables','exp','HP','choices','storyProgress', 'main_quest', 'side_quest', 'objective', 'helper', 'usedCraft', 'chests', 'AIdata'
    ];

    /**
     * The attributes that should be cast.
     *
     * @var array
     */
    protected $casts = [
        'main_items' => 'array',
        'items' => 'array',
    ];

    /**
     * Get the user that owns the player.
     */
    public function user()
    {
        return $this->belongsTo(User::class);
    }
}
