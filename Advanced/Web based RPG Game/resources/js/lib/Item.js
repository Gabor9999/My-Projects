class Item {
    constructor(src,itemtype,icon,atk = 0,def = 0,acc = 0,eva = 0) {
        this.img = new Image;
        this.src = src;
        this.itemtype = itemtype;
        this.atk = atk;
        this.def = def;
        this.acc = acc;
        this.eva = eva;
        this.img.src = this.src;
        this.icon = icon
    }

    get get_data() {
        return ['',
            this.src,
            this.itemtype,
            this.atk,
            this.def,
            this.acc,
            this.eva
        ];
    }

    get get_stats() {
        return [
            this.atk,
            this.def,
            this.acc,
            this.eva
        ];
    }
}

export function newItem(src,type,atk,def,acc,eva,icon) {
    return new Item(src,type,atk,def,acc,eva,icon);
};