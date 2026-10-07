const stations = [
    {
        name: "Radio Dominicana 1",
        genre: "Música Tropical",
        url: "https://TU-STREAM-AQUI"
    },
    {
        name: "Radio Dominicana 2",
        genre: "Bachata",
        url: "https://TU-STREAM-AQUI"
    },
    {
        name: "Radio Dominicana 3",
        genre: "Merengue",
        url: "https://TU-STREAM-AQUI"
    },
    {
        name: "Radio Dominicana 4",
        genre: "Urbana",
        url: "https://TU-STREAM-AQUI"
    }
];

let currentStation = null;

let favorites = JSON.parse(
    localStorage.getItem("favorites") || "[]"
);

const audio = document.getElementById("audio");

function renderStations(list = stations) {

    const container = document.getElementById("stations");

    container.innerHTML = "";

    list.forEach((station, index) => {

        const div = document.createElement("div");

        div.className = "station";

        const favorite =
            favorites.includes(index);

        div.innerHTML = `
            <div class="station-icon">
                📻
            </div>

            <div class="info">
                <h3>${station.name}</h3>
                <p>${station.genre}</p>
            </div>

            <div
                class="favorite ${favorite ? "active" : ""}"
                onclick="toggleFavorite(event, ${index})"
            >
                ♥
            </div>
        `;

        div.addEventListener(
            "click",
            () => playStation(index)
        );

        container.appendChild(div);
    });
}

function playStation(index) {

    const station = stations[index];

    currentStation = index;

    audio.src = station.url;

    document.getElementById(
        "currentName"
    ).textContent = station.name;

    document.getElementById(
        "status"
    ).textContent = "Conectando...";

    audio.play()
        .then(() => {

            document.getElementById(
                "status"
            ).textContent =
                "🔴 Reproduciendo en vivo";

            document.getElementById(
                "playButton"
            ).textContent = "❚❚";

        })
        .catch(() => {

            document.getElementById(
                "status"
            ).textContent =
                "No se pudo conectar";

        });
}

function togglePlay() {

    if (currentStation === null) {
        playStation(0);
        return;
    }

    if (audio.paused) {

        audio.play();

        document.getElementById(
            "playButton"
        ).textContent = "❚❚";

    } else {

        audio.pause();

        document.getElementById(
            "playButton"
        ).textContent = "▶";
    }
}

function changeVolume(value) {
    audio.volume = value;
}

function toggleFavorite(event, index) {

    event.stopPropagation();

    if (favorites.includes(index)) {

        favorites =
            favorites.filter(
                item => item !== index
            );

    } else {

        favorites.push(index);
    }

    localStorage.setItem(
        "favorites",
        JSON.stringify(favorites)
    );

    renderStations();
}

function searchStations() {

    const query =
        document
            .getElementById("search")
            .value
            .toLowerCase();

    const filtered =
        stations.filter(station =>
            station.name
                .toLowerCase()
                .includes(query) ||
            station.genre
                .toLowerCase()
                .includes(query)
        );

    renderStations(filtered);
}

audio.addEventListener(
    "error",
    () => {

        document.getElementById(
            "status"
        ).textContent =
            "⚠️ Error con la señal";

        document.getElementById(
            "playButton"
        ).textContent = "▶";
    }
);

renderStations();