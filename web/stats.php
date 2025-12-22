<?php
/*
 * Copyright 2025 Tom Coombs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
ini_set('display_errors', 1);
ini_set("log_errors", 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

$statsFolder = '/home/MCServer/Weihnachtsminecraft Welt/stats/';
$getPlayer = isset($_GET['player']) ? $_GET['player'] : null;
$stats = null;

function formatTime($ticks)
{
    $minutes = floor($ticks / 20 / 60);
    $hours = floor($minutes / 60);
    $minutes = $minutes % 60;
    return $hours . 'h ' . $minutes . 'm';
}

function formatUUIDWithDashes($uuid)
{
    return substr($uuid, 0, 8) . '-' .
        substr($uuid, 8, 4) . '-' .
        substr($uuid, 12, 4) . '-' .
        substr($uuid, 16, 4) . '-' .
        substr($uuid, 20, 12);
}

function getUUID($player)
{
    $cacheFile = __DIR__ . '/uuid_cache.json';
    $cache = file_exists($cacheFile) ? json_decode(file_get_contents($cacheFile), true) : [];

    if (isset($cache[$player])) return $cache[$player];

    $url = "https://api.mojang.com/users/profiles/minecraft/" . urlencode($player);
    $json = @file_get_contents($url);
    if (!$json) return null;

    $data = json_decode($json, true);
    if (!isset($data['id'])) return null;

    $uuid = formatUUIDWithDashes($data['id']);
    $cache[$player] = $uuid;
    file_put_contents($cacheFile, json_encode($cache, JSON_PRETTY_PRINT));

    return $uuid;
}

function loadPlayerJson($player)
{
    global $statsFolder;

    $uuid = getUUID($player);
    if (!$uuid) return null;

    $file = $statsFolder . $uuid . '.json';
    if (!file_exists($file)) return null;

    $data = json_decode(file_get_contents($file), true);
    return $data;
}

if ($getPlayer) {
    $data = loadPlayerJson($getPlayer);
    if ($data && isset($data['stats'])) {
        $custom = $data['stats']['minecraft:custom'] ?? [];

        $stats = [
            'player' => $getPlayer,
            'playtime' => formatTime($custom['minecraft:play_time'] ?? 0),
            'deaths' => $custom['minecraft:deaths'] ?? 0,
            'mob_kills' => $custom['minecraft:mob_kills'] ?? 0,
            'player_kills' => $custom['minecraft:player_kills'] ?? 0,
            'damage_dealt' => $custom['minecraft:damage_dealt'] ?? 0,
            'damage_taken' => $custom['minecraft:damage_taken'] ?? 0,
            'jumps' => $custom['minecraft:jump'] ?? 0,
        ];
    }
}
?>
<!DOCTYPE html>
<html lang="de">

<head>
    <meta charset="UTF-8">
    <title><?php echo $getPlayer ? $getPlayer . "'s Jahresrückblick" : "Minecraft Stats Jahresrückblick"; ?></title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script type="text/javascript">
        function copyLink() {
            const linkInput = document.getElementById('shareLink');
            linkInput.select();
            linkInput.setSelectionRange(0, 99999);
            navigator.clipboard.writeText(linkInput.value);

            const feedback = document.getElementById('copyFeedback');
            feedback.classList.add('show');
            setTimeout(() => feedback.classList.remove('show'), 1500);
        }

        function showPlayer() {
            window.location.href = "https://gympw-mc.de/review/" + document.getElementById("player").value;
        }
    </script>

    <style>
        body {
            min-height: 100vh;
            margin: 0;

            background-image: url('https://i.imgur.com/7BjxfxL.png');
            background-repeat: no-repeat;
            background-position: center center;
            background-size: cover;
        }

        body::before {
            content: '';
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.45);
            backdrop-filter: blur(10px);
            z-index: -1;
        }

        .copy-feedback {
            position: absolute;
            top: -30px;
            right: 0;
            opacity: 0;
            transition: opacity 0.3s ease, transform 0.3s ease;
            transform: translateY(-10px);
        }

        .copy-feedback.show {
            opacity: 1;
            transform: translateY(0);
        }
    </style>
</head>

<body class="font-sans">

    <div class="container mx-auto p-6">
        <h1 class="text-4xl font-bold mb-6 text-center text-gray-100 drop-shadow-lg">Minecraft Jahresrückblick</h1>

        <div class="mb-8 flex justify-center gap-3">
            <input type="text" name="player" id="player" placeholder="Spielername" class="border rounded px-3 py-2 w-64 shadow focus:outline-none focus:ring-2 focus:ring-blue-500">
            <button type="button" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition" onclick="showPlayer();">Stats ansehen</button>
        </div>

        <?php if ($stats): ?>
            <div class="bg-white/80 shadow-lg rounded-xl p-8 max-w-xl mx-auto relative overflow-hidden backdrop-blur-sm">
                <!-- Minecraft Avatar -->
                <div class="flex justify-center mb-4">
                    <img src="https://minotar.net/avatar/<?php echo htmlspecialchars($stats['player']); ?>/100.png" alt="Avatar" class="rounded-full border-4 border-blue-500">
                </div>

                <h2 class="text-2xl font-bold mb-6 text-center text-gray-800"><?php echo htmlspecialchars($stats['player']); ?>'s Jahresrückblick</h2>

                <ul class="grid grid-cols-2 gap-4 text-gray-700">
                    <li class="bg-gray-50/80 p-3 rounded shadow hover:bg-gray-100 transition"><span class="font-semibold">Spielzeit:</span> <?php echo $stats['playtime']; ?></li>
                    <li class="bg-gray-50/80 p-3 rounded shadow hover:bg-gray-100 transition"><span class="font-semibold">Tode:</span> <?php echo $stats['deaths']; ?></li>
                    <li class="bg-gray-50/80 p-3 rounded shadow hover:bg-gray-100 transition"><span class="font-semibold">Mob-Kills:</span> <?php echo $stats['mob_kills']; ?></li>
                    <li class="bg-gray-50/80 p-3 rounded shadow hover:bg-gray-100 transition"><span class="font-semibold">Spieler-Kills:</span> <?php echo $stats['player_kills']; ?></li>
                    <li class="bg-gray-50/80 p-3 rounded shadow hover:bg-gray-100 transition"><span class="font-semibold">Damage ausgeteilt:</span> <?php echo $stats['damage_dealt']; ?></li>
                    <li class="bg-gray-50/80 p-3 rounded shadow hover:bg-gray-100 transition"><span class="font-semibold">Damage erhalten:</span> <?php echo $stats['damage_taken']; ?></li>
                    <li class="bg-gray-50/80 p-3 rounded shadow hover:bg-gray-100 transition col-span-2"><span class="font-semibold">Sprünge:</span> <?php echo $stats['jumps']; ?></li>
                </ul>

                <div class="mt-6 text-center relative">
                    <p class="mb-2 text-gray-700">Teile diesen Link:</p>
                    <div class="flex justify-center items-center gap-2 relative">
                        <input type="text" readonly id="shareLink" class="border rounded px-3 py-2 w-full text-center shadow" value="<?php echo htmlspecialchars((isset($_SERVER['HTTPS']) ? "https" : "http") . "://" . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI']); ?>">
                        <button onclick="copyLink()" class="bg-green-500 text-white px-3 py-2 rounded hover:bg-green-600 transition">Kopieren</button>
                        <span id="copyFeedback" class="copy-feedback text-green-600 font-semibold">Kopiert!</span>
                    </div>
                </div>
            </div>
        <?php elseif ($getPlayer): ?>
            <div class="bg-red-100 border border-red-400 text-red-700 p-4 rounded max-w-lg mx-auto shadow">
                Spieler <?php echo htmlspecialchars($getPlayer); ?> nicht gefunden.
            </div>
        <?php endif; ?>
    </div>
</body>

</html>