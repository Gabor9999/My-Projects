class Character {
    constructor(type, HP, maxHP, skills, stats) {
        this.type = type;
        // 0: Friendly
        // 1+: Enemy
        this.HP = HP;
        this.maxHP = maxHP;
        this.skills = skills;
        this.defended = false;
        if(type == 0) {
            this.defense = 0.25;
        } else if (type == 1) {
            this.defense = 0.4;
        } else if (type == 2) {
            this.defense = 0.25;
        } else if (type == 3 || type == 4) {
            this.defense = 0.1;
            this.thorns = false;
        }
        this.stunned = false;
        this.willStun = false;
        this.willPosion = false;
        this.willHeal = false;
        this.poisonCount = 0;
        this.attackedCount = 0;
        this.atk = stats[0];
        this.def = stats[1];
        this.acc = stats[2];
        this.eva = stats[3];
    }

    full_heal() {
        this.stunned = false;
        this.poisonCount = 0;
        this.HP = this.maxHP;
    }

    setValues() {
        if(this.type == 0) {
            this.defense = 0.25;
            if(this.defended) {this.eva -= 10;}
        } else {
            this.willPosion = false;
            this.willStun = false;
            if (this.type == 1) {
                this.defense = 0.4;
            } else if (this.type == 2) {
                this.defense = 0.25;
            } else if (this.type == 3 || this.type == 4) {
                this.defense = 0.1;
                this.thorns = false;
            }
        }
        this.defended = false;
    }
}

export function createChar(type, HP, maxHP, skills, stats) {
    return new Character(type, HP, maxHP, skills, stats);
}