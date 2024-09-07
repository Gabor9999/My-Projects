import * as ml5 from 'ml5';

let trainingData = [];
let classifier = ml5.neuralNetwork({
        task: 'classification',
        debug: false
    });
    
export async function loadTrainingData(AIdata) {
    // Betanítani a korábbi adatokat, megcímkézve a kimenetet
    // Train the classifier, label the outputs
    trainingData = [
        //HP%, poisonCount, typeOfAction, atk, def, acc, eva, potions, enemyType
        { inputs: [0.8, 1, 0, 100,130,1,2, 4, 1], output: [1, 1, 1, 1, 0, 0, 0, 0, 0, 0] }, // Normal atk, Heavy atk, Bash, Poison, 0/2x/4x, Normal def, Stun def, Posion def, Thorns, Negate hit, Heal
        { inputs: [0.45, 0, 0, 100,100,0,1, 0, 1], output: [1, 1, 1, 1, 0, 0, 0, 0, 0, 0] },
        { inputs: [0.33, 0, 0, 110,111,3,2, 2, 1], output: [1, 1, 1, 1, 0, 0, 0, 0, 0, 0] },
        { inputs: [0.3, 2, 1, 120,120,2,5, 2, 2], output: [1, 0, 1, 1, 0, 0, 0, 0, 0, 0] },
        { inputs: [0.76, 0, 1, 110,100,2,3, 0, 2], output: [1, 0, 1, 1, 0, 0, 0, 0, 0, 0] },
        { inputs: [0.5, 0, 0, 140,125,3,4, 2, 3], output: [1, 1, 1, 1, 1, 0, 0, 0, 0, 0] },
        { inputs: [0.77, 0, 0, 143,116,5,2, 3, 1], output: [0, 0, 0, 0, 0, 1, 1, 1, 0, 0] },
        { inputs: [0.88, 0, 0, 120,104,3,1, 2, 2], output: [0, 0, 0, 0, 0, 1, 1, 1, 0, 0] },
        { inputs: [0.99, 0, 0, 115,131,2,6, 2, 1], output: [0, 0, 0, 0, 0, 1, 1, 1, 0, 0] },
        { inputs: [0.08, 2, 0, 133,111,2,1, 1, 3], output: [0, 0, 0, 0, 0, 1, 1, 1, 1, 1] }
    ];
    AIdata.forEach((inputData) => {
        trainingData.push(inputData)
    })

    trainingData.forEach(data => {
        classifier.addData(data.inputs, data.output);
    });

    classifier.normalizeData();
    let finished = false;
    classifier.train({ epochs: 50 }, () => {classifier.ready = true;finished = true});
    while(1==1) {
        if(finished) {
            return true;
        }
        await sleep(250);
    }
}

const sleep = ms => new Promise(r => setTimeout(r, ms));

export function makeAIDecision(playerData) {
    //Hozzon az adatok alapján döntést, majd értelmezzük és határozzunk meg egy cselekedetet
    //Make a decision based on data, then process the result (in Fight)
    const prediction = classifier.predict(playerData);
    return prediction;
}