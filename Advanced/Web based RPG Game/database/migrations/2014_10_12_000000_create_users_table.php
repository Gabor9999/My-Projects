<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('users', function (Blueprint $table) {
            $table->id();
            $table->string('name');
            $table->string('email')->unique();
            $table->timestamp('email_verified_at')->nullable();
            $table->string('password');
            $table->rememberToken();
            $table->timestamps();
            $table->tinyInteger('difficulty')->default('2');
            $table->integer('text_size')->default('12');
        });

        Schema::create('players', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->onDelete('cascade');
            $table->tinyInteger('chapter');
            $table->integer('storyProgress')->nullable();
            $table->integer('main_quest')->nullable();
            $table->integer('objective')->nullable();
            $table->json('side_quest')->nullable();
            $table->json('chests')->nullable();
            $table->integer('position_x');
            $table->integer('position_y');
            $table->integer('potions');
            $table->json('consumables')->nullable();
            $table->integer('HP')->default(100);
            $table->integer('exp')->default(0);
            $table->integer('choices')->default(0);
            $table->integer('usedCraft')->default(0);
            $table->json('main_items')->nullable();
            $table->json('items')->nullable();
            $table->integer('helper')->nullable();
            $table->json('AIdata')->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('player');
        Schema::dropIfExists('users');
    }
};
