<?php
ini_set('display_errors', 1);
ini_set("log_errors", 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

$context = stream_context_create([
    'http' => [
        'method'  => 'GET',
        'header'  => "User-Agent: Minecraft-Stats-Page\r\n",
        'timeout' => 5
    ]
]);

$json = file_get_contents(
    'https://api.mcsrvstat.us/2/gympw-mc.de',
    false,
    $context
);

$data = json_decode($json, true);

if (!$data || !$data['online']) {
    $offline = true;
}

$serverIcon = $data['icon'] ?? null;
?>
<!DOCTYPE html>
<html lang="de">

<head>
    <meta charset="UTF-8">
    <title>Gym-PW Minecraft Server</title>
    <script src="https://cdn.tailwindcss.com"></script>
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
            inset: 0;
            background: rgba(0, 0, 0, 0.45);
            backdrop-filter: blur(10px);
            z-index: -1;
        }
    </style>
</head>

<body class="text-white font-sans">
    <div class="container mx-auto p-6 max-w-4xl">

        <h1 class="text-4xl font-bold text-center mb-8 drop-shadow-lg">
            🎄 Gym-PW Minecraft Server
        </h1>

        <?php if (!empty($offline)): ?>
            <div class="bg-red-600/80 p-6 rounded-xl text-center shadow-xl">
                ❌ Server ist aktuell offline
            </div>
        <?php else: ?>

            <div class="bg-white/90 text-gray-800 rounded-2xl shadow-2xl p-8 backdrop-blur">

                <!-- Status -->
                <div class="flex items-center justify-between mb-6">
                    <span class="text-lg font-semibold">
                        Status:
                        <span class="text-green-600 font-bold">ONLINE</span>
                    </span>
                    <span class="text-sm text-gray-500">
                        Version <?php echo htmlspecialchars($data['version']); ?>
                    </span>
                </div>

                <!-- MOTD & Icon -->
                <div class="flex mb-6">
                    <div class="w-full">
                        <h2 class="font-bold text-xl mb-2">Server-MOTD</h2>
                        <?php foreach ($data['motd']['clean'] as $line): ?>
                            <p><?php echo htmlspecialchars($line); ?></p>
                        <?php endforeach; ?>
                    </div>
                    <div class="flex justify-end w-full">
                        <?php if (!empty($serverIcon)): ?>
                            <img
                                src="<?php echo htmlspecialchars($serverIcon); ?>"
                                alt="Server Icon"
                                class="w-24 h-24 rounded-2xl shadow-xl border-4 border-white/60
                   hover:scale-105 transition-transform duration-300">
                        <?php endif; ?>
                    </div>
                </div>
                <!-- Stats -->
                <div class="grid grid-cols-2 gap-4 mb-6">
                    <div class="bg-gray-100 p-4 rounded-xl">
                        👥 Spieler online<br>
                        <span class="text-2xl font-bold">
                            <?php echo $data['players']['online']; ?>
                            /
                            <?php echo $data['players']['max']; ?>
                        </span>
                    </div>

                    <div class="bg-gray-100 p-4 rounded-xl">
                        🗺️ Welt<br>
                        <span class="font-semibold">
                            <?php echo htmlspecialchars($data['map']); ?>
                        </span>
                    </div>

                    <div class="bg-gray-100 p-4 rounded-xl col-span-2">
                        ⚙️ Software<br>
                        <?php echo htmlspecialchars($data['software']); ?>
                    </div>
                </div>

                <!-- Spieler -->
                <?php if (!empty($data['players']['list'])): ?>
                    <div class="mb-6">
                        <h2 class="font-bold text-xl mb-3">Online Spieler</h2>
                        <div class="flex flex-wrap gap-2">
                            <?php foreach ($data['players']['list'] as $player): ?>
                                <span class="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm">
                                    <?php echo htmlspecialchars($player); ?>
                                </span>
                            <?php endforeach; ?>
                        </div>
                    </div>
                <?php endif; ?>

                <!-- Plugins -->
                <?php if (!empty($data['plugins']['names'])): ?>
                    <div>
                        <h2 class="font-bold text-xl mb-3">Plugins</h2>
                        <ul class="list-disc list-inside text-sm text-gray-700">
                            <?php foreach ($data['plugins']['raw'] as $plugin): ?>
                                <li><?php echo htmlspecialchars($plugin); ?></li>
                            <?php endforeach; ?>
                        </ul>
                    </div>
                <?php endif; ?>
            </div>
        <?php endif; ?>
    </div>
</body>

</html>