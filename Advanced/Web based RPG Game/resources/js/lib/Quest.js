import main from "../../../resources/quests/main.json"
import side from "../../../resources/quests/side.json"

class Quest {
    constructor(type, number, objective, progress = 0) {
        this.type = type;
        // 0: Main
        // 1: Side
        this.mnumber = number;
        this.onumber = objective;
        if(this.type == 0) {
            this.quest = main[this.mnumber];
        } else {
            this.progress = progress;
            this.active = true;
            this.quest = side[this.mnumber];
        }
    }

    //Both

    get get_title() {
        return this.quest.title;
    }

    get get_objective() {
        return this.onumber;
    }

    //Main

    get get_desc() {
        return this.quest.objectives[this.onumber];
    }

    get get_main_quest() {
        return this.mnumber;
    }

    //Side

    get get_amount() {
        if(this.type == 1) {
            return side[this.mnumber].amount;
        }
    }

    get get_progress() {
        if(this.type == 1) {
            return this.progress;
        }
    }

    get get_active() {
        return this.active
    }

    get get_side_quest_data() {
        return [
            this.mnumber,
            this.onumber,
            this.progress
        ]
    }

    setMission(m,o) {
        this.mnumber = m
        if(this.type == 0) {
            this.quest = main[this.mnumber];
        }
        this.onumber = o;
        return this.quest.party;
    };

    next() {
        this.onumber = 0;
        this.mnumber++;
        if(this.type == 0) {
            this.quest = main[this.mnumber];
        }
        return this.quest.party;
    };

    progress(n) {
        this.progress += n
    };

    setActive() {
        this.active = false;
    };
}

export function newSideQuest(qnumber,objective,progress = 0) {
    return new Quest(1,qnumber,objective,progress);
}
export const main_quest = new Quest(0,0,0);