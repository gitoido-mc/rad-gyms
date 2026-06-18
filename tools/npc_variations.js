/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */
const { rm } = require('node:fs/promises');
const { writeFile } = require('node:fs');


const variations = [
    "aethergruntf",
    "aethergruntm",
    "aquagruntf",
    "aquagruntm",
    "arven_sv",
    "barry_dppt",
    "bianca_bw",
    "blue_frlg",
    "brendan_emerald",
    "calem_xy",
    "dawn_dp",
    "elio_usum",
    "flaregruntf",
    "flaregruntm",
    "galacticgruntf",
    "galacticgruntm",
    "gladion_sm",
    "gloria_swsh",
    "gold_gsc",
    "hau_sm",
    "hilbert_bw",
    "hilda_bw",
    "hop_swsh",
    "kris_crystal",
    "leaf_frlg",
    "lillie_sm",
    "lucas_platinum",
    "lyra_hgss",
    "magmagruntf",
    "magmagruntm",
    "marnie_swsh",
    "may_rs",
    "n_bw",
    "nate_bw2",
    "nemona_sv",
    "penny_sv",
    "plasmagruntf",
    "plasmagruntm",
    "red_frlg",
    "rocketgruntf",
    "rocketgruntm",
    "rosa_bw2",
    "selene_sunmoon",
    "serena_xy",
    "silver_hgss",
    "skullgruntf",
    "skullgruntm",
    "stargruntf",
    "stargruntm",
    "victor_swsh"
];

const trainerData = {
    "../common/src/main/resources/assets/rad_gyms/bedrock/npcs/variations/0_trainer_junior.json": {
        name: "rad_gyms:trainer_junior",
        order: 0,
        variations: [
            {
                aspects: [],
                poser: "cobblemon:standard",
                model: "cobblemon:steve.geo",
                texture: "cobblemon:textures/npcs/standard/trainer.png",
                layers: []
            }
        ]
    },
    "../common/src/main/resources/assets/rad_gyms/bedrock/npcs/variations/0_trainer_senior.json": {
        name: "rad_gyms:trainer_senior",
        order: 0,
        variations: [
            {
                aspects: [],
                poser: "cobblemon:standard",
                model: "cobblemon:steve.geo",
                texture: "cobblemon:textures/npcs/standard/trainer.png",
                layers: []
            }
        ]
    },
    "../common/src/main/resources/assets/rad_gyms/bedrock/npcs/variations/0_trainer_leader.json": {
        name: "rad_gyms:trainer_leader",
        order: 0,
        variations: [
            {
                aspects: [],
                poser: "cobblemon:standard",
                model: "cobblemon:steve.geo",
                texture: "cobblemon:textures/npcs/standard/trainer.png",
                layers: []
            }
        ]
    },
};

async function fillAspects(key, index, arr) {
    const data = trainerData[key];
    variations.forEach((value) => {
        data.variations.push({
            aspects: [value],
            texture: `rad_gyms:textures/npc/${value}.png`
        });
    })

    await rm(key, { force: true });
    writeFile(key, JSON.stringify(data, null, 2), {flag: "w+"}, (err) => {
        if (err) throw err;
    });
}

Object.keys(trainerData).forEach(fillAspects)
