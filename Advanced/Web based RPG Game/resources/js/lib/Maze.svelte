<script>
    import { getContext, onMount, createEventDispatcher } from 'svelte';
	import { collisions, battleground } from '../../../resources/js/collisions/collisions_maze';

    const dispatch = createEventDispatcher();

	let collisionsPos = [];
	let battlegroundPos = [];
	let chest_a = [[900,662,2],[2764,1042,1],[4108,1170,1],[1284,2070,2],[2180,3222,2],[4612,3222,2],[3980,3730,1],[4876,4370,1]]

	var map;
	for (let i = 0; i < collisions.length; i += 92) {
		map = collisions.slice(i,92 + i);
		map.forEach((symbol, j) => {
			if(symbol != 0) {
				const coor = [j*64, (i/92)*64]
				collisionsPos.push(coor);
			}
		})

		map = battleground.slice(i,92 + i);
		map.forEach((symbol, j) => {
			if(symbol != 0) {
				const coor = [j*64, (i/92)*64]
				battlegroundPos.push(coor);
			}
		})
	}

    let width = 1920;
	let height = 1088;

	export let canvas, ctx, drawInv, drawHMenu, showSide, pos_x, pos_y, chests, chapter, helper,wear_t, side_quests,showShader;
	var bg,chara,hair,wear,halfHeight,halfWidth,middleOfScreen, contains,chest_img;

	let moveLock = false;

    let dir = 2;
	let animationStep = 0;
	let place_w = 0;
	let place_h = -1952;
    var { locked } = getContext('flags');
	var locked_fl;
    locked.subscribe((value) => {locked_fl = value});

	onMount(async() => {
		ctx = canvas.getContext('2d');
		if(chests.length < 3) {
			chests.push([1,1,1,1,1,1,1,1])
		}
		if (pos_x != 999) {
			place_w = pos_x;
			place_h = pos_y;
		}
		bg = new Image();
		chara = new Image();
		hair = new Image();
		wear = new Image();
		chest_img = new Image();
        bg.onload = () => {
			halfHeight = canvas.height/2
			halfWidth = canvas.width/2;
			middleOfScreen = [(halfWidth - 96), halfHeight];
			draw()
			showShader = false;
        };
		bg.src = 'forest_maze.png';
		chara.src = 'player/player.png';
		hair.src = 'player/hair.png';
		if(wear_t < 2) {
			wear.src = 'player/underwear.png';
		} else if (wear_t < 4) {
			wear.src = 'player/armor1.png';
		} else {
			wear.src = 'player/armor2.png';
		}
		chest_img.src = 'RGW_Chests.png';
	});

	function drawCharacter(x,y) {
		ctx.drawImage(chara,
			animationStep * 64, dir * 64,
			64,64, 
			x, y,
			128,128)
		ctx.drawImage(hair,
			animationStep * 64,dir * 64,64,64,
			x + 8, y + 8, 112,112
		)
		ctx.drawImage(wear,
			animationStep * 64, dir * 64,64,64, 
			x, y,
			128,128)
	}

	function drawChests() {
		for (let i = 0; i < chest_a.length; i++) {
			let chest = chest_a[i];
			ctx.drawImage(chest_img,
				chests[2][i]*32, chest[2]*32,
				chest_img.width/12, chest_img.height/8,
				chest[0]+place_w, chest[1]+place_h,
				40, 40
			);
		}
	}

	const sleep = ms => new Promise(r => setTimeout(r, ms));

	function draw() {
		ctx.clearRect(0,0,canvas.width,canvas.height);
		ctx.drawImage(bg, place_w, place_h);
		drawChests()
		drawCharacter(middleOfScreen[0], middleOfScreen[1]);
	};

	function animNextStep() {
		if(animationStep == 5) {
			animationStep = 0
		} else {animationStep++}
	}

	function basicMove(move, d) {
		if(d) {
			place_w += move;
		} else { place_h += move;}
		if(middleOfScreen[0] - place_w - 32 == 832 && (middleOfScreen[1] - place_h + 32 >= 2368 && middleOfScreen[1] - place_h + 32 <= 2624)) {
			(async() => {
				locked.set(7);
				await sleep(500);
				chapter = 2;
				pos_x = -3200
				pos_y = -2112
				await sleep(500);
				locked.set(1)
			})()		
		}
		onBattleground();
	}

	//Check if an object the player facing can be interacted with
    function checkInteractable() {
		if (dir == 7) {
			for(let i = 0; i < chest_a.length; i++) {
				if(halfHeight - place_h + 54 === chest_a[i][1] && halfWidth - place_w - 124 === chest_a[i][0] && chests[2][i] == 1) {
					if(chest_a[i][0] == 900) {
						locked.set(0)
						helper++
					} else {
						dispatch('item')
					}
					chests[2][i] = 0;
					for(let i = 0; i < side_quests.length; i++) {
						if(side_quests[i].mnumber == 3) {
							if(chests[2].filter((x) => (x == 1)).length == 0) {
								dispatch('invitem', {item: 3})
								side_quests[i].setActive();
							} else {
								side_quests[i].progress++
							}
							break;
						}
					}
					return
				}
			}
		}
		else if (dir == 6) {
			for(let i = 0; i < chest_a.length; i++) {
				if(halfHeight - place_h + 50 === chest_a[i][1] && halfWidth - place_w + 12 === chest_a[i][0] && chests[2][i] == 1) {
					dispatch('item')
					chests[2][i] = 0;
					for(let i = 0; i < side_quests.length; i++) {
						if(side_quests[i].mnumber == 3) {
							side_quests[i].progress++
							break;
						}
					}
					return;
				}
			}
		}
	}

	//Check if player moved to a battle square
	function onBattleground() {
		contains = battlegroundPos.some(elem => {
			return JSON.stringify([middleOfScreen[0] - place_w + 32, middleOfScreen[1] - place_h + 32]) === JSON.stringify(elem);
		});
		if(contains) {
			const n = Math.random()
			if(n*100 < 20) {
				dispatch('fight', {"fm": 1});
				moveLock = true;
			}
		}
	}

	$: if(locked_fl == 4) {
		moveLock = false;
	}

	//Handle key press
    function onKeyDown(e) {
		if((locked_fl === 1 || locked_fl === 2) && !drawInv && !drawHMenu && !moveLock) {
			switch(e.keyCode) {
				case 87: //W
					contains = collisionsPos.some(elem =>{
						return JSON.stringify([middleOfScreen[0] - place_w + 32, middleOfScreen[1] - place_h - 32]) === JSON.stringify(elem);
					});
					dir = 5;
					if(!contains) {
						basicMove(64, false);
					}
					animNextStep()
					break;
				case 65: //A
					contains = collisionsPos.some(elem => {
						return JSON.stringify([middleOfScreen[0] - place_w - 32, middleOfScreen[1] - place_h + 32]) === JSON.stringify(elem);
					});
					dir = 7;
					if(!contains) {
						basicMove(64, true);
					}
					animNextStep()
					break;
				case 83: //S
					contains = collisionsPos.some(elem =>{
						return JSON.stringify([middleOfScreen[0] - place_w + 32, middleOfScreen[1] - place_h + 96]) === JSON.stringify(elem);
					});
					dir = 4;
					if(!contains) {
						basicMove(-64, false);
					}
					animNextStep()
					break;
				case 68: //D
					contains = collisionsPos.some(elem => {
						return JSON.stringify([middleOfScreen[0] - place_w + 96, middleOfScreen[1] - place_h + 32]) === JSON.stringify(elem);
					});
					dir = 6;
					if(!contains) {
						basicMove(-64, true);
					}
					animNextStep()
					break;
				case 69: //E
					checkInteractable();
					break;
				case 73: //I
					drawInv = true;
					break;
				case 81: //Q
					drawHMenu = true;
					break;
				case 89: //Y
					showSide = !showSide;
					break;
				case 27: //Esc
					pos_x = place_w;
					pos_y = place_h;
					locked.set(6)
					break;
				default:
					return;
			}
			draw();
		}
    }
</script>
<svelte:window on:keydown|preventDefault={onKeyDown}></svelte:window>
<canvas bind:this={canvas} {width} {height}>
	<slot />
</canvas>

<style>
	canvas {
		width: 100%;
		height: 100%;
		left: 0;
        right: 0;
        bottom: 0;
        top: 0;
        position: absolute;
	}
</style>