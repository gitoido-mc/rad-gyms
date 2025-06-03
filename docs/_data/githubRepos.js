import Fetch from "@11ty/eleventy-fetch";

export default async function () {
	let url = "https://api.github.com/repos/11ty/eleventy";

	let json = await Fetch(url, {
		duration: "1d", // save for 1 day
		type: "json", // we’ll parse JSON for you
	});

	return json;
};