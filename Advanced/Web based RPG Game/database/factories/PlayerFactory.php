<?php

namespace Database\Factories;

use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\Player>
 */
class PlayerFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'chapter' => 0,
            'storyProgress' => null,
            'main_quest' => null,
            'objective' => null,
            'side_quest' => null,
            'chests' => null,
            'position_x' => 999,
            'position_y' => 999,
            'potions' => 1,
            'consumables' => null,
            'helper' => 'integer',
            'HP' => 100,
            'exp' => 0,
            'choices' => 0,
            'usedCraft' => 0,
            'main_items' => null,
            'items' => null,
            'helper' => null,
            'AIdata' => null,
            'created_at' => now(),
            'updated_at' => now(),
        ];
    }
}
