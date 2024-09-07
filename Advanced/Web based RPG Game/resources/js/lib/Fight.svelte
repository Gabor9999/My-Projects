<script>
    import { onMount, getContext, createEventDispatcher } from 'svelte';
    import Button from './Button.svelte';
    import { createChar } from './Character';
    import { loadTrainingData, makeAIDecision } from './AI.js';
	import { main_quest } from '../lib/Quest';

    const dispatch = createEventDispatcher();

    let width = 1920;
	let height = 1080;

	export let canvas;
	var ctx;
    const fightBg = new Image();
    const chara = new Image();
	const hair = new Image();
	const wear = new Image();
    const orc_idle = new Image();
	const orc_shaman_idle = new Image();
	const chiefi = new Image();
	const comp = new Image();
	const op1 = new Image();
	const op2 = new Image();
	const op3 = new Image();

    export let showShader,difficulty,skills,fightmode, party, item, fightEvent,fightType, showHit, exp, charLvl, multF, helper, consumables, companionType, side_quests, main_atk, chapter, wear_t, player, companion, chief, AIdata, MCname;
    let healedThisTurn = false;
    let enemyTurn = false;
    let lock_h_option = false;
	let playerLocked = false;
	let showResultOfBattle = false;
	let sum = 1;
	let multiplierE = difficulty * 0.5;
	let multiplierF = 2- (difficulty * 0.5);
	let minAtk = multiplierF * Math.max((player.atk - 100)/10,1) + 6
    var { locked, helper_inv_heal, select_item } = getContext('flags');
    var locked_fl, inv_fl, selector, damageDid, drawn, damageToH,select, base_atk,roll,target,recievedExpThisBattle,recievedConsThisBattle,trainingFinished;
	locked.subscribe((value) => {locked_fl = value});
	helper_inv_heal.subscribe((value) => {inv_fl = value});
	select_item.subscribe((value) => {selector = value});
	let enemyLine = [null,null,null]; //drawing
	let enemies = [null,null,null]; //objects

	//Set up battle with the fight scene containing enemies and friendly characters, and also the options menu
    onMount(async() => {
		trainingFinished = await loadTrainingData(AIdata)
		select_item.set(0);
		recievedExpThisBattle = 0;
		recievedConsThisBattle = [0,0,0]
		ctx = canvas.getContext('2d');
		ctx.font = "20px Arial";
        ctx.fillRect(0,0,canvas.width, canvas.height);
        fightBg.onload = async () => {
            ctx.clearRect(0, 0, canvas.width, 7*canvas.height/10 - 2);
			ctx.drawImage(fightBg,0,0,canvas.width,7*canvas.height/10 - 2)
			drawCharacter(250, 7*canvas.height/20 -24)
			if(party == 1) {
            	drawChief(350, 7*canvas.height/20 -174);
        	} else if (party == 2) {
				if(companionType == 1) {
					comp.src = 'rogue.png'
				} else if (companionType == 2) {
					comp.src = 'knight.png'
				}
				comp.onload = () => {
					drawComp();
				}
			} else if (party == 3) {
				drawChief(350, 7*canvas.height/20 -174);
				if(companionType == 1) {
					comp.src = 'rogue.png'
				} else if (companionType == 2) {
					comp.src = 'knight.png'
				}
				comp.onload = () => {
					drawComp();
				}
			}
			select = (fightType[0] == 0) ? 1 : 0
			if(fightEvent > 0) {
				switch(fightEvent) {
					case 1:
						drawn = op1;
						enemies = [null,createChar(1,20,20,[0],[100,120,1,1]),null];
						break;
					case 2:
						drawn = op2;
						enemies = [null,createChar(1,50,50,[0],[130,120,1,1]),null];
						break;
					case 3:
						drawn = op3;
						enemies = [null,createChar(1,100,100,[0],[150,130,2,2]),null];
						break;
				}
				ctx.drawImage(drawn,
					0, 0,
					drawn.width, drawn.height,
					canvas.width - 361, 7*canvas.height/20 + 5,
					60,60);
			} else {
				for(let i = 0; i < 3;i++) {
					switch(fightType[i]) {
						case 0:
							enemies[i] = null;
							break;
						case 1:
							enemyLine[i] = new Image();
							let h1 = (i == 1) ? 7*canvas.height/20 : ((i == 0) ? canvas.height/10 : 3*canvas.height/5)
							enemyLine[i].onload = () => {
							flip();
							ctx.drawImage(enemyLine[i],
								0, 0,
								enemyLine[i].width/4, enemyLine[i].height,
								300, h1,
								64,64);
								flip();}	
							enemyLine[i].src = 'Orc_Idle-Sheet.png';
							enemies[i] = createChar(1,20,20,[0],[100,100,0,0]);
							break;
						case 2:
							enemyLine[i] = new Image();
							let h2 = (i == 1) ? 7*canvas.height/20 : ((i == 0) ? canvas.height/10 : 3*canvas.height/5)
							enemyLine[i].onload = () => {
							flip();
							ctx.drawImage(enemyLine[i],
								0, 0,
								enemyLine[i].width/4, enemyLine[i].height,
								300, h2,
								64,64);
								flip()}
							enemyLine[i].src = 'Orc_Shaman_Idle-Sheet.png';
							enemies[i] = createChar(2,50,50,[1],[80,80,0,5]);
							break;
						case 3:
							enemyLine[i] = new Image();
							let h3 = (i == 1) ? 7*canvas.height/20 : ((i == 0) ? canvas.height/10 : 3*canvas.height/5)
							enemyLine[i].onload = () => {
							flip();
							ctx.drawImage(enemyLine[i],
								0, 0,
								enemyLine[i].width/4, enemyLine[i].height,
								300, h3,
								64,64);
								flip()}
							enemyLine[i].src = 'Orc_Warrior_Idle-Sheet.png';
							enemies[i] = createChar(3,100,100,[0,1,2],[120,120,5,0]);
							break;
						case 4:
							enemyLine[i] = new Image();
							let h4 = (i == 1) ? 7*canvas.height/20 : ((i == 0) ? canvas.height/10 : 3*canvas.height/5)
							enemyLine[i].onload = () => {
							flip();
							ctx.drawImage(enemyLine[i],
								0, 0,
								enemyLine[i].width/4, enemyLine[i].height,
								300, h4,
								64,64);
								flip()}
							enemyLine[i].src = 'Orc_Warrior_Idle-Sheet.png';
							enemies[i] = createChar(4,500,500,[0,1,2],[150,150,10,5]);
							break;
					}
				}
			}
			drawFightOptions();
            locked.set(5);
			showShader = false;
        }
		fightBg.src = 'bg_fight_grass.png'
		chara.src = 'player/player.png';
		hair.src = 'player/hair.png'
		if(wear_t < 2) {
			wear.src = 'player/underwear.png';
		} else if (wear_t < 4) {
			wear.src = 'player/armor1.png';
		} else {
			wear.src = 'player/armor2.png';
		}
		orc_idle.src = 'Orc_Idle-Sheet.png';
		orc_shaman_idle.src = 'Orc_Shaman_Idle-Sheet.png';
		chiefi.src = 'chief.png'
		op1.src = 'woman_f1.png'
		op2.src = 'man_f2.png'
		op3.src = 'knight_f3.png'
    });

    function drawCharacter(x,y) {
		if(player.poisonCount > 0) {
			ctx.fillText('🧪', 200 + chara.width/10,2*canvas.height/5 - 10);
		}
		ctx.drawImage(chara,
			0,0,
			chara.width/8,chara.height/8, 
			x, y,
			chara.width/4,chara.height/4)
		ctx.drawImage(hair,
			0,0,hair.width/8, hair.height/8,
			x + 8, y + 8, hair.width/4 - 16,hair.height/4 - 16
		)
		ctx.drawImage(wear,
			0, 0,64,64, 
			x, y,
			128,128);
	}

	function drawChief(x,y) {
		if(chief.poisonCount > 0 && chief.HP != 0) {
			ctx.fillText('🧪',canvas.width/6,11*canvas.height/50);
		}
		ctx.drawImage(chiefi,
			0, 0,
			chiefi.width/4, chiefi.height,
			x, y,
			64,64);
	}

	function drawComp() {
		if(companion.poisonCount > 0 && companion.HP != 0) {
			ctx.fillText('🧪',canvas.width/6,11*canvas.height/20);
		}
		ctx.drawImage(comp,
			0, 0,
			comp.width, comp.height,
			350, 7*canvas.height/20 + 176,
			64,64);
	}

	function flip() {
		ctx.translate(canvas.width,0);
		ctx.scale(-1,1);
	}

	function drawOrc(s) {
		var h1;
		for(let i = 0; i < 3;i++) {
			if(enemyLine[i] != null && i != s) {
				if (enemies[i].poisonCount > 0 && enemies[i].HP != 0) {
					ctx.fillText('🧪', canvas.width - 290,2*canvas.height/15 + i*canvas.height/4 - 10);
				}
				if((enemies[i].type == 3 || enemies[i].type == 4) && enemies[i].thorns) {
					ctx.fillText('🌵', canvas.width - 290,2*canvas.height/15 + i*canvas.height/4 + 20);
				}
				h1 = (i == 1) ? 7*canvas.height/20 : ((i == 0) ? canvas.height/10 : 3*canvas.height/5)
				flip();
				ctx.drawImage(enemyLine[i],
					0, 0,
					enemyLine[i].width/4, enemyLine[i].height,
					300,h1,
					64,64);
				flip();
				}
		}
	}

	//Draw the options screen with names and HP
    function drawFightOptions() {
		ctx.fillStyle = "grey";
		ctx.fillRect(0, 7*canvas.height/10, canvas.width, 3*canvas.height/10);
		ctx.beginPath();
		ctx.moveTo(0, 7*canvas.height/10);
		ctx.lineTo(canvas.width, 7*canvas.height/10);
		ctx.lineWidth = 2
		ctx.stroke()
		ctx.lineWidth = 1;
		ctx.fillText('⭐', 250 + chara.width/10,3*canvas.height/10 + 15);
		ctx.rect(7*canvas.width/25 - 1, 17*canvas.height/20 - 1, canvas.width/5 + 1, 10);
		ctx.stroke();
		if(party == 1) {
			ctx.rect(7*canvas.width/25 - 1, 19*canvas.height/25 - 1, canvas.width/5 + 1, 10);
			ctx.stroke();
		}
		else if(party == 2) {
			ctx.rect(7*canvas.width/25 - 1, 47*canvas.height/50 - 1, canvas.width/5 + 1, 10);
            ctx.stroke();
		} else if (party == 3) {
			ctx.rect(7*canvas.width/25 - 1, 19*canvas.height/25 - 1, canvas.width/5 + 1, 10);
			ctx.stroke();
			ctx.rect(7*canvas.width/25 - 1, 47*canvas.height/50 - 1, canvas.width/5 + 1, 10);
            ctx.stroke();
		}
		updatePlayerHealth();
		updateEnemyHealth();
	}

	function updateEnemyHealth() {
		let i = 0;
		enemies.forEach((enemy) => {
			if(enemy != null) {
				ctx.clearRect(3*canvas.width/4, 19*canvas.height/25 + i*9*canvas.height/100, canvas.width/5, 8);
				ctx.fillStyle = "#6e6969";
				ctx.fillRect(3*canvas.width/4, 19*canvas.height/25 + i*9*canvas.height/100, canvas.width/5, 8);
				ctx.fillStyle = "red";
				ctx.fillRect(3*canvas.width/4, 19*canvas.height/25 + i*9*canvas.height/100, Math.max(enemy.HP/enemy.maxHP,0) * canvas.width/5, 8);
				ctx.lineWidth = 1.5;
				ctx.rect(3*canvas.width/4 - 1.5, 19*canvas.height/25 + i*9*canvas.height/100 - 1.5, canvas.width/5 + 1, 9.5);
				ctx.stroke();
			}
			i++;
		})
	}

	function updatePlayerHealth() {
		ctx.clearRect(7*canvas.width/25 + 1, 17*canvas.height/20, canvas.width/5 -2, 8);
		ctx.fillStyle = "#6e6969";
		ctx.fillRect(7*canvas.width/25 + 1, 17*canvas.height/20, canvas.width/5 -2, 8);
		ctx.fillStyle = "#1FB31F";
		ctx.fillRect(7*canvas.width/25 + 1, 17*canvas.height/20, player.HP/player.maxHP * canvas.width/5 -2, 8);
		if(party == 1) {
			ctx.clearRect(7*canvas.width/25 + 1, 19*canvas.height/25, canvas.width/5 -2, 8);
			ctx.fillStyle = "#6e6969";
			ctx.fillRect(7*canvas.width/25 + 1, 19*canvas.height/25, canvas.width/5 -2, 8);
			ctx.fillStyle = "#1FB31F";
			ctx.fillRect(7*canvas.width/25 + 1, 19*canvas.height/25, chief.HP/chief.maxHP * canvas.width/5 - 2, 8);
		}
		else if (party == 2) {
			ctx.clearRect(7*canvas.width/25 + 1, 47*canvas.height/50, canvas.width/5 -2, 8);
			ctx.fillStyle = "#6e6969";
			ctx.fillRect(7*canvas.width/25 + 1, 47*canvas.height/50, canvas.width/5 -2, 8);
			ctx.fillStyle = "#1FB31F";
			ctx.fillRect(7*canvas.width/25 + 1, 47*canvas.height/50, companion.HP/companion.maxHP * canvas.width/5 - 2, 8);
		}
		else if (party == 3) {
			ctx.clearRect(7*canvas.width/25 + 1, 19*canvas.height/25, canvas.width/5 -2, 8);
			ctx.clearRect(7*canvas.width/25 + 1, 47*canvas.height/50, canvas.width/5 -2, 8);
			ctx.fillStyle = "#6e6969";
			ctx.fillRect(7*canvas.width/25 + 1, 19*canvas.height/25, canvas.width/5 -2, 8);
			ctx.fillRect(7*canvas.width/25 + 1, 47*canvas.height/50, canvas.width/5 -2, 8);
			ctx.fillStyle = "#1FB31F";
			ctx.fillRect(7*canvas.width/25 + 1, 19*canvas.height/25, chief.HP/chief.maxHP * canvas.width/5 - 2, 8);
			ctx.fillRect(7*canvas.width/25 + 1, 47*canvas.height/50, companion.HP/companion.maxHP * canvas.width/5 - 2, 8);
		}
	}

	//Draw enemies and friendly characters
    function drawFightScene(opt,char = player) {
		ctx.clearRect(0, 0, canvas.width, 7*canvas.height/10 - 2);
		ctx.drawImage(fightBg,0,0,canvas.width,7*canvas.height/10 - 2)
		if(opt != 1) {
			drawCharacter(250, 7*canvas.height/20 -24)
		}
		if(party == 1) {
			if(char.maxHP != 90) {
				drawChief(350, 7*canvas.height/20 -174);
			}
        } else if (party == 2) {
			if(char.maxHP != 80) {
				drawComp();
			}
		} else if (party == 3) {
			if(char.maxHP != 90) {
				drawChief(350, 7*canvas.height/20 -174);
			}
			if(char.maxHP != 80) {
				drawComp();
			}
		}
		if(opt != 2) {
			if(fightEvent > 0) {
				ctx.drawImage(drawn,
					0, 0,
					drawn.width, drawn.height,
					canvas.width - 360, 7*canvas.height/20 +5,
					60,60);
			} else {
				drawOrc(4);
			}
		} else {
			drawOrc(select);
		}
	}

    const sleep = ms => new Promise(r => setTimeout(r, ms));

	//Handle events after players turn
    async function endTurn() {
		drawFightScene(0);
		select_item.set(0);
		if(!player.defended && !healedThisTurn && !player.stunned) {
			await animDamage(2);
		}
		player.stunned = false;
		player.attackedCount = 0;
		if(sum == 0) {return;}
		if(party == 1 || party == 3) {
			if(chief.defended) {
				chief.defended = false;
				chief.eva -= 10;
			}
			if(enemies[select].HP == 0) {
				chooseNextTarget()
			}
			if(chief.poisonCount > 0) {
				chief.HP -= Math.max(Math.floor(multiplierE * chief.HP/10),1)
				chief.poisonCount--;
				updatePlayerHealth();
				await animDamage(0,chief);
			}
			if(chief.stunned) {
				showHit = 6;
				showHitRes(0);
				await sleep(1000);
			} else {
				if(Math.random() < 0.6) {
					ctx.fillText('🪓', 365 ,7*canvas.height/20 -200);
					var at = (Math.random() < 0.8) ? 1:2
					if(at == 2 && Math.random() > 0.8) {
						at == 0
					}
					await sleep(2500);
					attackEnemy(chief,at)
					await animDamage(2);
				} else {
					ctx.fillText('🛡️', 365 ,7*canvas.height/20 -200);
					await sleep(2500);
					chief.defended = true;
					chief.eva += 10;
					drawFightScene(0)
				}
			}
		}
		if(sum == 0) {return;}
		if(party > 1) {
			if(companion.defended) {
				companion.defended = false;
				companion.eva -= 10;
			}
			if(enemies[select].HP == 0) {
				chooseNextTarget()
			}
			if(companion.poisonCount > 0) {
				companion.HP -= Math.max(Math.floor(multiplierE * companion.HP/10),1)
				companion.poisonCount--;
				updatePlayerHealth();
				await animDamage(0,companion);
			}
			if(companion.stunned) {
				showHit = 6;
				showHitRes(0);
				await sleep(1000);
			} else {
				if(Math.random() < 0.6) {
					if(companionType == 1) {
						ctx.fillText('🗡️', 365 ,7*canvas.height/20 + 124);
					} else {
						ctx.fillText('🪓', 365 ,7*canvas.height/20 + 124);
					}
					var at = (Math.random() < 0.8) ? 1:2
					if(at == 2 && Math.random() > 0.8) {
						at == 0
					}
					await sleep(2500);
					attackEnemy(companion,at)
					await animDamage(2);
				} else {
					ctx.fillText('🛡️', 365 ,7*canvas.height/20 + 124);
					await sleep(2500);
					companion.defended = true;
					companion.eva += 10;
					drawFightScene(0)
				}
			}
		}
		if(sum == 0) {return;}
		for(let i = 0; i < 3; i++) {
			if(enemies[i] != null) {
				if(enemies[i].HP != 0) {
					if(enemies[i].poisonCount > 0) {
						select = i;
						enemies[i].HP -= Math.max(Math.floor(multiplierF * enemies[i].HP/10),1)
						enemies[i].poisonCount--;
						await animDamage(2);
					}
					if(enemies[i].thorns) {
						enemies[i].thorns = false;
						drawFightScene(0)
					}
					enemies[i].setValues();
					if(!enemies[i].stunned) {
						//If Shaman willHeal
						if(enemies[i].type == 2 && enemies[i].willHeal == true) {
							ctx.fillText('🩹', canvas.width - 344,2*canvas.height/20 + i*canvas.height/4 - 35);
							let ind = 0;
							let perc = 1;
							enemies.forEach((enemy,index) => {
								if(enemy.HP/enemy.maxHP < perc && enemy.HP != 0) {
									ind = index;
									perc = enemy.HP/enemy.maxHP;
								}
							})
							if(perc != 1) {
								enemies[ind].HP += Math.min(Math.round(enemies[ind].maxHP/3),enemies[ind].maxHP-enemies[ind].HP)
								enemies[i].willHeal = false;
								updateEnemyHealth();
							}
							await sleep(2000)
							drawFightScene(0);
						} else {
							let prediction = null;
							var attack, stn, decision;
							if(enemies[i].type == 2 && Math.random() < 0.5) {
								attack = false;
								stn = 0.99;
							} else {
								if((party == 1 && chief.HP != 0) || (party == 3 && companion.HP == 0)) {
									target = (Math.random() < 0.6) ? player : chief;
								} else if((party == 2 && companion.HP != 0) || (party == 3 && chief.HP == 0)) {
									target = (Math.random() < 0.6) ? player : companion;
								} else if(party == 3 && companion.HP != 0 && chief.HP != 0) {
									const s = Math.random()
									target = (s < 0.3) ? chief : ((s < 0.7) ? player : companion);
								} else {
									target = player;
								}
								if(AIdata.length < 5) {
									attack = (Math.random() < 0.6) ? true : false;
								} else {
									let refValue = Math.random() * 100
									prediction = await makeAIDecision([target.HP/target.maxHP,target.poisonCount,Number(target.defended),target.atk,target.def,target.acc,target.eva,item,enemies[i].type])
									for(let i = 0; i < 10; i++) {
										if(prediction[i].value * 100 > refValue) {
											decision = i;
											break;
										} else {
											refValue -= prediction[i].value * 100;
										}
									}
									attack = (decision < 5) ? true : false
								}
							}
							if(attack) {
								ctx.fillText('⚔️', canvas.width - 344,2*canvas.height/20 + i*canvas.height/4 - 35);
								if (decision != null) {
									if(decision < 2) {
										damageToH = attackStrategies(enemies[i],decision)
									} else {
										if(enemies[i].skills.findIndex((elem) => elem == decision-2) != -1) {
											damageToH = enemySkills(target,enemies[i],decision-2)
										} else {
											damageToH = attackStrategies(enemies[i],0)
										}
									}
								} else {
									const s = Math.random();
									damageToH = (s < 0.5) ? attackStrategies(enemies[i],0): ((s < 0.8) ? attackStrategies(enemies[i],1) : enemySkills(target,enemies[i],Math.floor(Math.random()*enemies[i].skills.length)));
								}
							} else {
								damageToH = 0;
								ctx.fillText('🛡️', canvas.width - 344,2*canvas.height/20 + i*canvas.height/4 - 35);
								if (decision != null) {
									if (decision != 5){
										defensiveStrategies(enemies[i],decision-5)
									}
								} else {
									if(stn == 0.99) {
										defensiveStrategies(enemies[i],5)
									} else {stn = Math.random();}
									if(stn > 0.12) {
										if(stn <= 0.2) {
											defensiveStrategies(enemies[i],1)
										} else if (stn <= 0.28) {
											defensiveStrategies(enemies[i],2)
										} else if (stn <= 0.39) {
											defensiveStrategies(enemies[i],3)
										} else if (stn <= 0.5) {
											defensiveStrategies(enemies[i],4)
										} else {
											defensiveStrategies(enemies[i],5)
										}
									}
								}
								enemies[i].defended = true;
							}
							if(damageToH != 0) {
								const n = await enemyAttacked(enemies[i].atk,target,damageToH)
								if(n == 0) {
									updatePlayerHealth();
									if(target.maxHP == 100) {
										await animDamage(1);
									} else {await animDamage(0,target);}
									if(player.HP == 0) {
										ctx.fillStyle = "grey"
										ctx.fillRect(0, 7*canvas.height/10, canvas.width, 3*canvas.height/10);
										select_item.set(-1)
										showHit = 4
										return;
									}
								} else if (n == 1){
									drawFightScene(0);
									showHit = 5;
									showHitRes(0)
								}
							} else {
								await sleep(1500);
								drawFightScene(0);
								if(enemies[i].defended == false) {
									showHit = 2;
									showHitRes(1)
								}	
							}
						}
					} else {
						showHit = 6;
						showHitRes(0)
						enemies[i].stunned = false;
					}
					if(enemies[i].type == 3 || enemies[i].type == 4) {
						enemies[i].attackedCount = 0;
					}
				}
			}
		}
		if(enemies[select].HP == 0) {
			chooseNextTarget()
		}
		player.setValues()
		if(player.poisonCount > 0) {
			player.HP -= Math.max(Math.floor(multiplierE * player.HP/10),1)
			player.poisonCount--;
			updatePlayerHealth();
			await animDamage(1);
		}
		if(player.stunned) {
			showHit = 6;
			showHitRes(0);
			await sleep(2000)
			endTurn();
		} else {
			playerLocked = false;
			ctx.fillText('⭐', 250 + chara.width/10,3*canvas.height/10 + 15);
			healedThisTurn = false;
		}
	}

	function attackStrategies(enemy,stn) {
		switch(stn) {
			case 0:
				base_atk = Math.max((enemy.atk - 100)/10 + charLvl,8);
				roll = Math.round(multiplierE * Math.random() * (base_atk-4) +4);
				return roll;
			case 1:
				if(Math.random() < 0.75) {
					base_atk = Math.max((enemy.atk - 100)/10 + 5 + charLvl,12);
					roll = Math.round(multiplierE * Math.random() * (base_atk-8) +8);
					return roll;
				} else {return 0;}
		}
	}

	function enemySkills(char,enemy,skn) {
		switch(skn) {
			case 0:
				if(Math.random() < 0.45) {
					char.stunned = true;
					base_atk = Math.max((enemy.atk - 100)/10 + charLvl,8);
					roll = Math.round(multiplierE *Math.random() * (base_atk-4) +4);
					return roll;
				} else {return 0}
			case 1:
				if(Math.random() < 0.55) {
					char.poisonCount = Math.round(Math.random() * 2 + 1)
					base_atk = Math.max((enemy.atk - 100)/10 + charLvl,8);
					roll = Math.round(multiplierE *Math.random() * (base_atk-4) +4);
					return roll;
				} else {return 0}
			case 2:
				base_atk = Math.max((enemy.atk - 100)/10 + charLvl,8);
				roll = 0;
				if(enemy.HP < enemy.maxHP/4) {
					roll += 2*Math.round(multiplierE * Math.random() * (base_atk-4) +4);
				}
				if(enemy.attackedCount >= 2) {
					roll += 2*Math.round(multiplierE * Math.random() * (base_atk-4) +4);
				}
				return roll;
		}
	}
			
	function defensiveStrategies(enemy, stn) {
		switch(stn) {
			case 1:
				enemy.willStun = true;
				break;
			case 2:
				enemy.willPoison = true;
				break;
			case 3:
				if(enemy.type == 3 || enemy.type == 4) {
					enemy.thorns = true;
				}
				break;
			case 4:
				if(enemy.type == 3 || enemy.type == 4) {
					enemy.defense = 0;
				}
				break;
			case 5:
				if(enemy.type == 2) {
					enemy.defense = 0.5;
					enemy.willHeal = true;
				}
				break;
		}
	}

	function chooseNextTarget() {
		for(let i = 0; i < 3;i++) {
			if(enemies[i] != null) {
				if(enemies[i].HP != 0) {
					select = i;
					break;
				}
			}
		}
	}

    async function animDamage(s, char = 0) {
		await sleep(100)
		drawFightScene(s,char)
		await sleep(300)
		drawFightScene(0)
		await sleep(300)
		drawFightScene(s,char)
		await sleep(300)
		drawFightScene(0)
	}

    $: if(inv_fl == 4) {
		if(item == 0) {
			lock_h_option = true;
		}
		updatePlayerHealth();
		helper_inv_heal.set(0);
	}

    function onKeyDown(e) {
        if (locked_fl === 5 && !playerLocked) {
			switch(e.keyCode) {
				case 13: // Enter
					if(selector == 0) {
						select_item.set(4)
					} else if (selector == 2) {
						select_item.set(5);
					} else if (selector == 3) {
						if(trainingFinished) {
							for(let i = 0;i < 3;i++) {
								if(enemies[i] != null) {
									if(enemies[i].HP == 0) {
										exp += fightType[i] * 50;
									}
								}		
							}
							locked.set(1);
						}
					} else if (selector == 4) {
						select_item.set(6);
					} else if (selector == 5) {
						select_item.set(12);
					} else {
						if (selector == 1) {
							player.defended = true;
							player.eva += 10;
						} else if (selector == 6) {
							attackEnemy(player,1)
						} else if (selector == 7) {
							if(Math.random()*100 > 80 + player.acc) {
								healedThisTurn = true; // Aka not dealing damage
								attackEnemy(player,0)
							} else {
								attackEnemy(player,2)
							}
						} else if (selector == 8) {
							/*enemy.stunned*/
							if(Math.random()*100 > 60 + player.acc) {
								healedThisTurn = true; // Aka not dealing damage
								attackEnemy(player,0)
							} else {
								attackEnemy(player,3)
							}
						} else if (selector == 9) {
							/*poison*/
							if(Math.random()*100 > 55 + player.acc) {
								healedThisTurn = true; // Aka not dealing damage
								attackEnemy(player,0)
							} else {
								attackEnemy(player,4)
							}
						} else if (selector == 10) {
							/*eye for an eye*/
							attackEnemy(player,5)
						} else if (selector > 10) {
							healedThisTurn = true;
							dispatch('heal', {bench: false, char: (selector - 11)});
						}
						playerLocked = true;
						endTurn();
					}
					break;
				case 27: // Esc
					if (selector > 3) {
						select_item.set(0)
					}
					break;
				case 38: // ^
					if(selector > 10) {
						if((selector == 12 && (party == 1 || party == 3)) || (selector == 13 && party > 1)) {
							select_item.update((n) => (n-1))
						} 
					} else if(selector == 4) {
						for(let i = select-1; i >= 0; i--) {
							if(enemies[i] != null) {
								if(enemies[i].HP != 0) {
									select = i;
									break;
								}
							}
						}
					} else if(selector != 0 && selector != 5 && selector != 6) {
						if(selector == 3 && lock_h_option) {
							select_item.update((n) => (n-2))
						} else if (selector > 7) {
							for(let i = selector-8; i >= 0; i--) {
								if(skills[i] == true) {
									select_item.set(i+7)
									break;
								}
							}
							break;
						} else {
							select_item.update((n) => (n-1))
						}
					}
					break;
				case 40: // ˇ
					if(selector > 10) {
						if((selector == 11 && (party == 1 || party == 3)) || (selector == 12 && party > 1)) {
							select_item.update((n) => (n+1))
						}
					} else if(selector == 4) {
						for(let i = select+1; i < 3; i++) {
							if(enemies[i] != null) {
								if(enemies[i].HP != 0) {
									select = i;
									break;
								}
							}
						}
					} else if(selector != 3 && selector != 5) {
						if (lock_h_option && selector == 1 && $fightmode == 1) {
							select_item.update((n) => (n+2))
						} else if (selector >= 6) {
							for(let i = selector-6; i < skills.length; i++) {
								if(skills[i] == true) {
									select_item.set(i+7)
									break;
								}
							}
							break;
						} else if(!(($fightmode == 0 && selector == 2) || (lock_h_option && selector == 1))){
							select_item.update((n) => (n+1))
						}
					}
					break;
				default:
					return;
			}
		}
    }

	async function attackEnemy(char,am) {
        enemyTurn = false;
		switch(am) {
			case 0:
				showHit = 2;
				showHitRes(0);
				return;
			case 1:
				base_atk = Math.max((char.atk - 100)/10,1) + 6;
				roll = multiplierF * Math.random() * (main_atk+1) +base_atk;
				if(enemies[select].defended) {
					roll *= enemies[select].defense;
				}
				roll = Math.round(roll)
				enemies[select].HP -= roll;
				damageDid = roll;
				showHit = 1;
				break;
			case 2:
				base_atk = Math.max((char.atk - 100)/10,1) + 10;
				roll = multiplierF * Math.random() * (main_atk+1) +base_atk;
				if(enemies[select].defended) {
					roll *= enemies[select].defense;
				}
				roll = Math.round(roll)
				enemies[select].HP -= roll;
				damageDid = roll;
				showHit = 1;
				break;
			case 3:
				/*stun*/
				enemies[select].stunned = true;
				base_atk = Math.max((char.atk - 100)/10,1) + 6;
				roll = 0.75 * multiplierF * Math.random() * (main_atk+1) +base_atk;
				if(enemies[select].defended) {
					roll *= enemies[select].defense;
				}
				roll = Math.round(roll)
				enemies[select].HP -= roll;
				damageDid = roll;
				showHit = 1;
				break;
			case 4:
				/*poison*/
				enemies[select].poisonCount = Math.round(Math.random() * 2 + 1);
				base_atk = Math.max((char.atk - 100)/10,1) + 6;
				roll = 0.5 * multiplierF * Math.random() * (main_atk+1) +base_atk;
				if(enemies[select].defended) {
					roll *= enemies[select].defense;
				}
				roll = Math.round(roll)
				enemies[select].HP -= roll;
				damageDid = roll;
				showHit = 1;
				break;
			case 5:
				/*attacked twice*/
				base_atk = Math.max((char.atk - 100)/10,1) + 6;
				roll = 0;
				if(char.HP < char.maxHP/4) {
					roll += 2*multiplierF * Math.random() * (main_atk+1) +base_atk;
				}
				if(player.attackedCount >= 2) {
					roll += 2*multiplierF * Math.random() * (main_atk+1) +base_atk;
				}
				if(enemies[select].defended) {
					roll *= enemies[select].defense;
				}
				roll = Math.round(roll)
				if(roll > 0) {
					enemies[select].HP -= roll;
					damageDid = roll;
					showHit = 1;
				} else {
					healedThisTurn = true;
					showHit = 2;
					showHitRes(0);
					return;
				}
				break;
		}
		updateEnemyHealth()
		if(enemies[select].type == 3 || enemies[select].type == 4) {
			if(enemies[select].thorns) {
				char.HP -= Math.max(enemies[select].atk - char.def,5)
				updatePlayerHealth()
				if(char.maxHP == 100) {
					player = player;
				} else if(char.maxHP == 90) {
					chief = chief;
				} else {
					companion = companion;

				}
			}
			enemies[select].attackedCount++;
		}
		if(enemies[select].willPoison) {
			char.poisonCount = Math.round(Math.random() * 2 + 1);
			enemies[select].willPoison = false;
		}
		if(enemies[select].willStun) {
			char.stunned = true;
			enemies[select].willStun = false;
		}
		if(char.maxHP == 100) {
			var outputs,type;
			type = enemies[select].type;
            if(char.defended) {
                if(type == 1) {
                    outputs = [1, 1, 1, 1, 0, 0, 0, 0, 0, 0]
                } else if(type == 2) {
                    outputs = [1, 0, 1, 1, 0, 0, 0, 0, 0, 0]
                } else if (type >= 3) {
                    outputs = [1, 1, 1, 1, 1, 0, 0, 0, 0, 0]
                }
            } else {
                if(type < 3) {
                    outputs = (Math.random() < 0.5) ? [0, 0, 0, 0, 0, 1, 1, 1, 0, 0] : [1, 1, 1, 0, 0, 0, 0, 0, 0, 0]
                } else if (type >= 3) {
                    outputs = (Math.random() < 0.5) ? [0, 0, 0, 0, 0, 1, 1, 1, 1, 0] : [1, 1, 1, 1, 1, 0, 0, 0, 0, 0]
                }
            }
			if(AIdata.length > 29) {
				AIdata.shift()
			}
			AIdata.push({ 
				inputs: [char.HP/char.maxHP,char.poisonCount,Number(char.defended),char.atk,char.def,char.acc,char.eva,item,type],
				output: outputs
			})
		}
		sum = 0;
		enemies.forEach((enemy) => {
			if(enemy != null) {
				if(enemy.HP <= 0) {
					enemy.HP = 0;
				} else {
					sum++;
				}
			}
		})
		if(sum == 0) {
			await sleep(1500);
			for(let i = 0;i < 3;i++) {	
				if(fightType[i] != null) {
					exp += fightType[i] * 50;
					recievedExpThisBattle += fightType[i] * 50;
					let cons_amount1 = Math.round(Math.random() * fightType[i])
					if(cons_amount1 > 0) {
						consumables[0] += cons_amount1
						recievedConsThisBattle[0] += cons_amount1
					}
					if(fightType[i] == 1 && Math.random() < 0.15) {
						consumables[1]++;
						recievedConsThisBattle[1]++
					}
					if(fightType[i] == 2 && Math.random() < 0.12) {
						consumables[2]++;
						recievedConsThisBattle[2]++
					}
				}
			}
			if($fightmode == 0) {
				if(fightEvent > 0) {
					fightEvent++
					player.HP = player.maxHP;
				}
			} else {
				for(let i = 0; i < side_quests.length; i++) {
					if(side_quests[i].mnumber == 0 && chapter == 1) {
						side_quests[i].progress += 1
					} else if ((side_quests[i].mnumber == 1 && chapter == 2) || side_quests[i].mnumber == 5) {
						side_quests[i].progress += fightType.filter((x) => (x == 2)).length
					} else if ((side_quests[i].mnumber == 2 && chapter == 2) || side_quests[i].mnumber == 6) {
						side_quests[i].progress += fightType.filter((x) => (x == 3)).length
					} else if (side_quests[i].mnumber == 4) {
						side_quests[i].progress += fightType.filter((x) => (x == 1)).length
					}
					if(side_quests[i].get_progress >= side_quests[i].get_amount) {
						exp += side_quests[i].quest.reward
						if(side_quests[i].mnumber == 0) {
							helper++;
							main_quest.onumber++;
						} else if(side_quests[i].mnumber == 4) {
							dispatch('invitem', {item: 4})
						} else if(side_quests[i].mnumber == 5) {
							dispatch('invitem', {item: 5})
						} else if(side_quests[i].mnumber == 6) {
							dispatch('invitem', {item: 6})
						}
						side_quests.splice(i,1)
					}
				}
			}
			showHit = 0;
			showResultOfBattle = true;
			ctx.fillStyle = "grey"
			ctx.fillRect(0, 7*canvas.height/10, canvas.width, 3*canvas.height/10);
			playerLocked = true;
			select_item.set(-1)
		}
		showHitRes(0);
    }

	async function showHitRes(type){
		if(type == 0) {
			enemyTurn = false
		} else {
			enemyTurn = true
		}
        await sleep(2500);
        showHit = 0;
    }

	async function enemyAttacked(enemy_atk,target,d) {
        await sleep(3000);
        enemyTurn = true;
		let damage = d;
		if(target.defended) {
			let lower = Math.max(enemy_atk-target.def,0);
			damage = Math.round(Math.random() * (damage/2-lower) + lower);
		}
		if(Math.random() * 100 < target.eva || damage == 0) {
			return 1;
		} else {
			damageDid = damage;
        	showHit = 1;
		}
		if(target.HP - d > 0) {
			target.attackedCount++;
			target.HP -= damage;
			if(target.maxHP == 100) {
				player = player;
			} else if(target.maxHP == 90) {
				chief = chief;
			} else {
				companion = companion;
			}
			showHitRes(1);
			return 0;
		} else {
			damageDid = damage;
        	showHit = 1;
			target.HP = 0;
			return 0;
		}
    }

	function getEnemyName(type) {
		if(fightEvent > 0) {
			return "Fighter"
		}
		switch(type) {
			case 1:
				return "Orc";
			case 2:
				return "Orc Shaman";
			case 3:
				return "Orc Warrior";
			case 4:
				return "Greater Orc Warrior";
		}
	}

	function restartBattle() {
		showHit = 0;
		select_item.set(0)
		enemies.forEach((enemy) => {
			if(enemy != null) {
				enemy.full_heal();
			}
		})
		player.HP = player.maxHP;
		if(party == 1) {
			chief.HP = chief.maxHP
		} else if(party == 2) {
			companion.HP = companion.maxHP
		} else if (party == 3) {
			chief.HP = chief.maxHP
			companion.HP = companion.maxHP
		}
		if(item == 0) {
            if(item == 0) {
				lock_h_option = true;
			}
			updatePlayerHealth();
        }
		drawFightOptions();
		drawFightScene(0);
		updateEnemyHealth()
		updatePlayerHealth()
		ctx.fillText('⭐', 250 + chara.width/10,3*canvas.height/10 + 15);
		playerLocked = false;
	}

	function endBattle() {
		if($fightmode == 0 && !multF) {
			locked.set(0);
		} else if(helper == 21 && chapter == 2) {
			locked.set(0);
			helper++;
		} else {locked.set(1);}
		if(player.defended) {
			player.defended = false;
			player.eva -= 10;
		}
		if(party == 1 || party == 3) {
			if(chief.defended) {
				chief.defended = false;
				chief.eva -= 10;
			}
		}
		if(party > 1) {
			if(companion.defended) {
				companion.defended = false;
				companion.eva -= 10;
			}
		}
	}
</script>
<svelte:window on:keydown|preventDefault={onKeyDown}></svelte:window>
<canvas bind:this={canvas} {width} {height}>
	<slot />
</canvas>
{#if showHit == 1}<div class:result={!enemyTurn} class:promo={enemyTurn}><table style="width: 100%"><tr><td>Hit!</td></tr><tr><td>Damage: {damageDid}</td></tr></table></div>
{:else if showHit == 2}<div class:result={!enemyTurn} class:promo={enemyTurn}>Missed!</div>
{:else if showHit == 4}<div class="promo">You lost</div>
<div class="buttons">
	<Button class="primary" on:click={restartBattle}>Restart battle</Button>
	<a href="/">
		<Button class="primary">
			Back To Menu
		</Button>
	</a>
</div>
{:else if showHit == 5}<div class:result={!enemyTurn} class:promo={enemyTurn}>Evaded</div>
{:else if showHit == 6}<div class:result={!enemyTurn} class:promo={enemyTurn}>Stunned</div>
{/if}
{#if selector > 10}
<div class="action-buttons">
	{#if party == 1 || party == 3}
	<div class="action_elem">{#if selector==11}<u>Chief</u>{:else}Chief{/if}</div>
	{/if}
	<div class="action_elem">{#if selector==12}<u>{MCname}</u>{:else}{MCname}{/if}</div>
	{#if party > 1}
	<div class="action_elem">{#if selector==13}<u>Trainee</u>{:else}Trainee{/if}</div>
	{/if}
</div>
{:else if selector > 5}
<div class="hit-chance" class:hit-chance2={select==0}>Hit chance: {#if selector==6 || selector==10 }100
	{:else if selector == 7}{80 + player.acc}
	{:else if selector == 8}{60 + player.acc}
	{:else if selector == 9}{55 + player.acc}
	{/if}%
</div>
<div class="action-buttons">
    <div class="action_elem">{#if selector==6}<u>Normal Attack</u>{:else}Normal Attack{/if} [{Math.floor(minAtk)}-{Math.ceil(minAtk)+main_atk+1}]</div>
    {#if skills[0]}<div class="action_elem">{#if selector==7}<u>Slash</u>{:else}Slash{/if} [{Math.floor(minAtk)+4}-{Math.ceil(minAtk)+main_atk+5}]</div>{/if}
	{#if skills[1]}<div class="action_elem">{#if selector==8}<u>Bash</u>{:else}Bash{/if} [{Math.floor(0.75 * minAtk)}-{Math.ceil(0.75 * (minAtk+main_atk+1))}]</div>{/if}
	{#if skills[2]}<div class="action_elem">{#if selector==9}<u>Poison Blade</u>{:else}Poison Blade{/if} [{Math.floor(0.5 * minAtk)}-{Math.ceil(0.5 * (minAtk+main_atk+1))}]</div>{/if}
	{#if skills[3]}<div class="action_elem">{#if selector==10}<u>Eye for an Eye</u>{:else}Eye for an Eye{/if} [0-{Math.ceil(4 * (minAtk+main_atk+1))}]</div>{/if}
</div>
{:else if selector > 4 && item != 0}
<div class="action-buttons">
    <div class="action_elem">{#if selector==5} <table class="item-table" style="width: 100%"> <tr><td style="text-align: center"><u>Potion</u></td> <td>x{item}</td></tr></table>{:else}<table> <tr><td>Potion</td> <td style="text-align: right">x1</td></tr></table>{/if}</div>
</div>
{:else if selector > 3}
<div class="action-buttons">
	{#each enemies as enemy,index}
		{#if enemy != null}
			<div class="action_elem">{#if select == index}<u>{getEnemyName(enemy.type)} [{index+1}]</u>{:else}{getEnemyName(enemy.type)} [{index+1}]{/if}</div>
		{/if}
	{/each}
</div>
{:else if selector != -1}
<div class="action-buttons">
    <div class="action_elem">{#if selector==0}<u>Attack</u>{:else}Attack{/if}</div>
    <div class="action_elem">{#if selector==1}<u>Defend</u>{:else}Defend{/if}</div>
    <div class="action_elem" class:action-locked={item==0} id="items">{#if selector==2 && item != 0}<u>Items</u>{:else}Items{/if}</div>
    <div class="action_elem" class:action-locked={$fightmode==0}>{#if selector==3 && fightmode != 0}<u>Escape</u>{:else}Escape{/if}</div>
</div>
{/if}
{#if selector != -1}
<div class="arrow-container" class:arrow-container2={select==0} class:arrow-container3={select==2}>
    <img src="arrow-icon.png" alt="Arrow to enemy health" width="45%" height="45%">
</div>
<table class="enemy-types">
	{#each enemies as enemy}
		{#if enemy != null}
		<tr><td>{getEnemyName(enemy.type)}</td></tr>
		<tr><td><br></td></tr>
		<tr><td>{enemy.HP}/{enemy.maxHP}</td></tr>
		{:else}
		<tr><td><br></td></tr>
		<tr><td><br></td></tr>
		<tr><td><br></td></tr>
		{/if}
	{/each}
</table>
{#if party == 1 || party == 3}
<table class="character-names" style="top: 72%">
	<tr><td>Chief</td></tr>
	<tr><td><br></td></tr>
	<tr><td>{chief.HP}/{chief.maxHP}</td></tr>
</table>
{/if}
<table class="character-names" style="top: 81%">
	<tr><td>{MCname}</td></tr>
	<tr><td><br></td></tr>
	<tr><td>{player.HP}/{player.maxHP}</td></tr>
</table>
{#if party > 1}
<table class="character-names" style="top: 90%">
	<tr><td>Trainee</td></tr>
	<tr><td><br></td></tr>
	<tr><td>{companion.HP}/{companion.maxHP}</td></tr>
</table>
{/if}
{/if}
{#if showResultOfBattle}
	<div class="results">
		<h2>Exprience recieved: {recievedExpThisBattle}</h2>
		<h2>Consumables recieved: {#if recievedConsThisBattle.filter(x => x > 0) == 0} - {/if}</h2>
		<div class="itemsRecieved" style="margin-left: 5%; font-size: 120%">
			{#if recievedConsThisBattle[0] > 0} - Monster Essence x{recievedConsThisBattle[0]}{/if}<br/>
			{#if recievedConsThisBattle[1] > 0} - Leather x1{/if}<br/>
			{#if recievedConsThisBattle[2] > 0} - Strings x1{/if}<br/>
		</div>
	</div>
	<div class="continue">
		<Button class="primary" style="height: 100%;" on:click={endBattle}>Continue</Button>
	</div>
{/if}
<style>
	.character-names {
        z-index: 2;
        position: absolute;
        left: 28%;
        top: 74%;
        width: 20%;
		height: 9%;
        text-align: center;
    }
	canvas {
		width: 100%;
		height: 100%;
		left: 0;
        right: 0;
        bottom: 0;
        top: 0;
        position: absolute;
	}
	.results {
		position: absolute;
		top: 72%;
		left: 25%;
		height: 10%;
	}
	.continue {
		position: absolute;
		top: 79%;
		left: 60%;
		width: 24%;
	}
	.buttons {
		position: absolute;
		top: 72%;
		left: 30%;
		width: 40%;
		height: 10%;
	}
	/* Harcablak akciógombjai */
    .action-buttons {
        top: 75%;
        left: 3%;
        z-index: 2;
        position: absolute;
        width: 20%;
    }
    .action_elem {
        padding-top: 10px;
        padding-bottom: 10px;
        border: 1px solid black;
        background-color: aqua;
        margin-bottom: 5px;
        text-align: center;
    }
    .action-locked {
        padding-top: 10px;
        padding-bottom: 10px;
        border: 1px solid black;
        background-color: aqua;
        margin-bottom: 5px;
        text-align: center;
        opacity: 0.7;
    }
    .hit-chance {
        z-index: 2;
        position: absolute;
        left: 56%;
        top: 75%;
        color: aliceblue;
        font-size: 18px;
    }
	.hit-chance2 {
        z-index: 2;
        position: absolute;
        left: 56%;
        top: 84%;
        color: aliceblue;
        font-size: 18px;
    }
	.enemy-types {
        z-index: 2;
        position: absolute;
        left: 75%;
        top: 72%;
		height: 27%;
        width: 20%;
        text-align: center;
    }
	.result {
        z-index: 2;
        position: absolute;
        text-align: center;
        width: 20%;
        left: 40%;
        margin-top: 2%;
        font-size: 22px;
        color: rgb(41, 151, 41);
        background: rgb(25, 25, 25);
        border: 1px solid rgb(41, 151, 41);
    }
	.promo {
        z-index: 2;
        position: absolute;
        text-align: center;
        width: 20%;
        left: 40%;
        margin-top: 2%;
        font-size: 22px;
        color: red;
        background: rgb(25, 25, 25);
        border: 1px solid red;
    }
    .arrow-container {
        z-index: 2;
        position: absolute;
        left: 52%;
        top: 73%
    }
	.arrow-container2 {
        z-index: 2;
        position: absolute;
        left: 52%;
        top: 64%
    }
	.arrow-container3 {
        z-index: 2;
        position: absolute;
        left: 52%;
        top: 82%;
    }
</style>
