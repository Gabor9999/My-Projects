<script>
    import { getContext, onMount, createEventDispatcher } from 'svelte';
	import { collisions, battleground } from '../../../resources/js/collisions/collisions_ch1'

	const dispatch = createEventDispatcher();

	let collisionsPos = [];
	const benchesPos = [[4160, 576],[4160, 640],[896, 2432],[960, 2432]]
	const chest_a = [[3562,982,1],[1216,2688,0],[3754,2454,1]]
	let battlegroundPos = [];
	var map;
	for (let i = 0; i < collisions.length; i += 81) {
		map = collisions.slice(i,81 + i);
		map.forEach((symbol, j) => {
			if(symbol != 0) {
				const coor = [j*64, (i/81)*64]
				collisionsPos.push(coor);
			}
		})

		map = battleground.slice(i,81 + i);
		map.forEach((symbol, j) => {
			if(symbol != 0) {
				const coor = [j*64, (i/81)*64]
				battlegroundPos.push(coor);
			}
		})
	}

	collisionsPos = collisionsPos.concat([[3904, 1408],[3968, 1408],[4032, 1408],[4096, 1408],[4160, 1408]]);
    
    let width = 1920;
	let height = 1088;

	export let canvas, ctx, drawInv, helper, drawHMenu, animCount, progress, pos_x, pos_y, gathering, loadAssets,showSide,chests,chiefMenu, chapter,wear_t,showShader;

	var bg,boxes,foreground,foreground_boxes,chara,hair,wear,chest_img,orc_idle,orc_shaman_idle,orc_rogue_idle,orc_warrior_idle,boss_img,pile_of_wood,rocks,chief,calcX,calcY;

	let drawBox = true;
	let chiefTrade = false;
	let moveLock = false;

    let dir = 0;
	let animationStep = 0;
	let place_w = 0;
	let place_h = 0;
	var { locked, helper_inv_heal, select_item} = getContext('flags');
	var locked_fl, inv_fl,selector, middleOfScreen, contains, exitTownPos, halfWidth, halfHeight;
	locked.subscribe((value) => {locked_fl = value});
	helper_inv_heal.subscribe((value) => {inv_fl = value});
	select_item.subscribe((value) => {selector = value});

	let enemies = [];
	let resources = [];

    onMount(async() => {
		ctx = canvas.getContext('2d');
		if(chests.length == 0) {
			chests.push([1,1,1])
		}
		if (pos_x != 999) {
			place_w = pos_x;
			place_h = pos_y;
		}
		bg = new Image();
		boxes = new Image();
		foreground = new Image();
		foreground_boxes = new Image();
		chara = new Image();
		hair = new Image();
		wear = new Image();
		chest_img = new Image();
		orc_idle = new Image();
		orc_shaman_idle = new Image();
		orc_rogue_idle = new Image();
		orc_warrior_idle = new Image();
		boss_img = new Image();
		pile_of_wood = new Image();
		rocks = new Image();
		chief = new Image();
		if(helper > 2) {
			drawBox = false;
			collisionsPos.splice(collisionsPos.length-5,5);
			if(helper > 7) {
				chiefTrade = true;
				collisionsPos.push([2560,1472])
				chief.onload = () => {ctx.drawImage(chief,
					0, 0,
					chief.width/4, chief.height,
					2560+place_w, 1472+place_h,
					64,64);}
			}
		} else if (helper == 2) {
			exitTownPos = [];
			for(let i = 1984; i < 2560; i += 64) {
				exitTownPos.push([i,2432]);
				exitTownPos.push([i,1408]);
			}
			collisionsPos = collisionsPos.concat(exitTownPos);
			enemies.push([2048,1600]);
			enemies.push([2368,2176]);
			drawEnemies();
		}
        bg.onload = () => {
			halfHeight = canvas.height/2
			halfWidth = canvas.width/2;
			middleOfScreen = [(halfWidth - 96), halfHeight];
			if(gathering || loadAssets) {
				if(animCount == "3") {
					resources = [[896,1280,0],[896,3136,1],[3712,2688,0],[1152,576,1],[3648,1856,0],[4160,2944,1]]
				}
				loadAssets = false;
			}
			draw()
			showShader = false;
        };
		bg.src = 'map_ch1.png';
		boxes.src = 'map_ch1_boxes.png'
		foreground.src = 'map_ch1_foreground.png'
		foreground_boxes.src = 'map_ch1_box_foreground.png'
		chara.src = 'player/player.png';
		hair.src = 'player/hair.png'
		if(wear_t < 2) {
			wear.src = 'player/underwear.png';
		} else if (wear_t < 4) {
			wear.src = 'player/armor1.png';
		} else {
			wear.src = 'player/armor2.png';
		}
		chest_img.src = 'RGW_Chests.png';
		orc_idle.src = 'Orc_Idle-Sheet.png';
		orc_shaman_idle.src = 'Orc_Shaman_Idle-Sheet.png';
		orc_rogue_idle.src = 'Orc_Rogue_Idle-Sheet.png';
		orc_warrior_idle.src = 'Orc_Warrior_Idle-Sheet.png';
		boss_img.src = 'world_boss.png';
		pile_of_wood.src = 'pile_of_wood.png';
		rocks.src = 'rocks.png';
		chief.src = 'chief.png';
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

	function drawChief() {
		ctx.drawImage(chief,
			0, 0,
			chief.width/4, chief.height,
			2560+place_w, 1472+place_h,
			64,64);
	}

	function drawOrc(x,y,type) {
		switch(type) {
			case 0:
				ctx.drawImage(orc_idle,
					0, 0,
					orc_idle.width/4, orc_idle.height,
					x, y,
					64,64
				)
				break;
			case 1:
				ctx.drawImage(orc_shaman_idle,
					0, 0,
					orc_idle.width/4, orc_idle.height,
					x, y,
					64,64
				)
				break;
			case 2:
				ctx.drawImage(orc_rogue_idle,
					0, 0,
					orc_idle.width/4, orc_idle.height,
					x, y,
					64,64
				)
				break;
			case 3:
				ctx.drawImage(orc_warrior_idle,
					0, 0,
					orc_idle.width/4, orc_idle.height,
					x, y,
					64,64
				)
				break;
		}
	}


	function drawChests() {
		for (let i = 0; i < chest_a.length; i++) {
			let chest = chest_a[i];
			ctx.drawImage(chest_img,
				chests[0][i]*32, chest[2]*32,
				chest_img.width/12, chest_img.height/8,
				chest[0]+place_w, chest[1]+place_h,
				40, 40
			);
		}
	}

	function drawEnemies() {
		for(let i = 0; i < enemies.length;i++) {
			drawOrc(enemies[i][0] + place_w,enemies[i][1] + place_h,0);
		}
	}

	function drawResources() {
		for(let i = 0; i < resources.length;i++) {
			if(resources[i][2] == 0) {
				ctx.drawImage(pile_of_wood,
				0, 0,
				pile_of_wood.width, pile_of_wood.height,
				resources[i][0]+place_w, resources[i][1]+place_h,
				86, 86
				);
			} else {
				ctx.drawImage(rocks,
				0, 0,
				rocks.width, rocks.height,
				resources[i][0]+place_w, resources[i][1]+place_h,
				64, 64
				);
			}
		}
	}

	function draw() {
		ctx.clearRect(0, 0, canvas.width, canvas.height)
		ctx.drawImage(bg, place_w, place_h);
		if(drawBox) {
			ctx.drawImage(boxes, place_w, place_h);
		}
		drawChests();
		drawEnemies();
		drawResources();
		if(chiefTrade) {
			drawChief() 
		}
		drawCharacter(middleOfScreen[0], middleOfScreen[1]);
		ctx.drawImage(foreground,place_w,place_h);
		if(drawBox) {
			ctx.drawImage(foreground_boxes,place_w,place_h);
		}
	};

	const sleep = ms => new Promise(r => setTimeout(r, ms));
	async function enemiesPopUp() {
		await sleep(1200);
		for(let i = 0; i < 29; i++) {
			const w = Math.random();
			const h = Math.random();
			let type = 0;
			if(w+h < 0.45) {
				type = 2;
			} else if (w+h < 0.65) {
				type = 1;
			}
			drawOrc(100+(w*(canvas.width-200)),(350+(h*halfHeight)), type)
			await sleep(250);
		}
		const w = Math.random();
		const h = Math.random();
		drawOrc(100+(w*(canvas.width-200)),(350+(h*halfHeight)),3)
	}

	$: if(locked_fl == 3) {
		switch(animCount) {
			case "0":
				enemies.push([2048,1600]);
				enemies.push([2368,2176]);
				drawEnemies();
				locked.set(1);
				break;
			case "1":
				ctx.clearRect(0,0,canvas.width, canvas.height);
				ctx.drawImage(boss_img,-1760,-400);
				drawCharacter(middleOfScreen[0], 100);
				(async() => {
					await enemiesPopUp()
					place_h += 64;
					dir = 5;
					locked.set(0);
				})();
				break;
			case "2":
				(async() => {
					locked.set(7);
					await sleep(1000);
					draw()
				})()
				break;
			case "3":
				resources = [[896,1280,0],[896,3136,1],[3712,2688,0],[1152,576,1],[3648,1856,0],[4160,2944,1]]
				drawResources()
				locked.set(1);
				break;
		}
	}

    function basicMove(move, d) {
		if(d) {
			place_w += move;
		} else { place_h += move;}
		switch(helper) {
			case 0:
				if (middleOfScreen[0] - place_w == 1952 && (middleOfScreen[1] - place_h >= 544 && middleOfScreen[1] - place_h <= 800)) {
					locked.set(0);
					helper++;
				}
				break;
			case 1:
				if ((middleOfScreen[0] - place_w >= 2016 && middleOfScreen[0] - place_w <= 2464) && middleOfScreen[1] - place_h == 1440) {
					locked.set(0);
					helper++;
					exitTownPos = [];
					for(let i = 1984; i < 2560; i += 64) {
						exitTownPos.push([i,2432]);
						exitTownPos.push([i,1408]);
					}
					collisionsPos = collisionsPos.concat(exitTownPos);
				}
				break;
			case 2:
				contains = enemies.findIndex(elem => {
					return JSON.stringify([middleOfScreen[0] - place_w + 32, middleOfScreen[1] - place_h + 32]) === JSON.stringify(elem);
				});
				if(contains != -1) {
					dispatch('fight', {"fm": 0});
					enemies.splice(contains, 1);
					return;
				}
				if(enemies.length == 0) {
					collisionsPos.splice(collisionsPos.length-25,25);
					drawBox = false;
					helper++;
					locked.set(0);
				}
				break;
			case 4:
				if((middleOfScreen[0] - place_w + 96 >= 3776 && middleOfScreen[0] - place_w + 96 <= 4160) && middleOfScreen[1] - place_h + 32 == 3200) {
					locked.set(0);
					helper++;
				}
				return;
			case 6:
				contains = resources.findIndex(elem => {
					return JSON.stringify([middleOfScreen[0] - place_w + 32, middleOfScreen[1] - place_h + 32]) === JSON.stringify([elem[0],elem[1]]);
				});
				if(contains != -1) {
					progress++;
					if(progress == 6) {
						gathering = false;
						progress = 0;
						locked.set(0);
						helper++;
					}
					resources.splice(contains, 1);
					return;
				}
				break;
			case 7:
				if ((middleOfScreen[0] - place_w >= 2016 && middleOfScreen[0] - place_w <= 2464) && (middleOfScreen[1] - place_h == 1440 || middleOfScreen[1] - place_h == 2336)) {
					locked.set(0);
					chiefTrade = true;
					collisionsPos.push([2560,1472])
					helper++
				}
				break;
			case 3:
			case 5:
			case 9:
			case 14:
				if ((middleOfScreen[0] - place_w >= 2016 && middleOfScreen[0] - place_w <= 2464) && (middleOfScreen[1] - place_h == 1440 || middleOfScreen[1] - place_h == 2336)) {
					locked.set(0);
					helper++;
				}
				break;
		}
		if((middleOfScreen[0] - place_w + 96 >= 3840 && middleOfScreen[0] - place_w + 96 <= 4224) && middleOfScreen[1] - place_h + 32 == 3200) {
			if(helper > 9) {
				(async() => {
					locked.set(7);
					await sleep(500);
					chapter = 2;
					pos_x = -3008;
					pos_y = -64;
					await sleep(500);
					locked.set(1)
				})()
			} else {
				if(d) {
					place_w -= move;
				} else { place_h -= move;}
			}
		}
		onBattleground();
	}

	function animNextStep() {
		if(animationStep === 5) {
			animationStep = 0
		} else {animationStep++}
	}

	function checkInteractable() {
		if (dir == 5) {
			calcX = middleOfScreen[0] - place_w + 32;
			calcY = middleOfScreen[1] - place_h - 32;
			contains = benchesPos.some(elem =>{
				return JSON.stringify([calcX, calcY]) === JSON.stringify(elem);
			});
			if(contains) {
				dispatch('heal', {bench: true});
				return;
			}
			for(let i = 0; i < chest_a.length; i++) {
				if(halfHeight - place_h + 32 === chest_a[i][1] && halfWidth - place_w - 64 === chest_a[i][0] && chests[0][i] == 1) {
					dispatch('item')
					chests[0][i] = 0;
					return
				}
			}
			if(calcX == 2560 && calcY == 1472) {
				if(helper == 15 || helper == 17 || helper == 18 || helper == 20 || helper == 22 || helper == 23) {
					helper++;
					locked.set(0)
				} else {chiefMenu = true}	
			}
		}
		else if (dir == 6) {
			calcX = middleOfScreen[0] - place_w + 96;
			calcY = middleOfScreen[1] - place_h + 32;
			contains = benchesPos.some(elem => {
				return JSON.stringify([calcX, calcY]) === JSON.stringify(elem);
			});
			if(contains) {
				dispatch('heal', {bench: true});
				return;
			}
			for(let i = 0; i < chest_a.length; i++) {
				if(halfHeight - place_h + 54 === chest_a[i][1] && halfWidth - place_w - 22 === chest_a[i][0] && chests[0][i] == 1) {
					dispatch('item')
					chests[0][i] = 0;
					return;
				}
			}
			if(calcX == 2560 && calcY == 1472) {
				if(helper == 15 || helper == 17 || helper == 18 || helper == 20 || helper == 22 || helper == 23) {
					helper++;
					locked.set(0)
				} else {chiefMenu = true}	
			}
		} else if (dir == 7) {
			if(middleOfScreen[0] - place_w - 32 == 2560 && middleOfScreen[1] - place_h + 32 == 1472) {
				if(helper == 15 || helper == 17 || helper == 18 || helper == 20 || helper == 22 || helper == 23) {
					helper++;
					locked.set(0)
				} else {chiefMenu = true}	
			}
		}
	}

	function onBattleground() {
		contains = battlegroundPos.some(elem => {
			return JSON.stringify([middleOfScreen[0] - place_w + 32, middleOfScreen[1] - place_h + 32]) === JSON.stringify(elem);
		});
		if(contains) {
			const n = Math.random()
			if(n*100 < 20) {
				moveLock = true;
				dispatch('fight', {"fm": 1});
			}
		}
	}

	$: if(locked_fl == 4) {
		moveLock = false;
	}

    function onKeyDown(e) {
		if((locked_fl === 1 || locked_fl === 2) && !drawInv && !drawHMenu && !chiefMenu && !moveLock) {
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
<svelte:window on:keydown={onKeyDown}></svelte:window>
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