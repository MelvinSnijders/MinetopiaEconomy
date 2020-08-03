![MinetopiaEconomy](https://img.themelvin.nl/8kffg.png)

## Over
MinetopiaEconomy is de oplossing voor een op SQL gebaseerde economy plugin. MinetopiaEconomy is gebouwd voor Minetopia servers die niet altijd gebruik willen maken van de economy in Essentials of die hun economy over verschillende servers (bungee) willen laten werken.

__Features__:
* MySQL / JSON opslag.
* Data wordt live gecached over (eventueel) verschillende Bungee servers.
* Alle database gerelateerde actie's zijn Async (met CompletableFuture).
* Bevat alle commands die nodig zijn om de economie te beheren.
* Oude data van *Essentials* wordt automatisch hersteld.

## Download
De laatste versie  van MinetopiaEconomy is beschikbaar op [Spigot](https://www.spigotmc.org/resources/minetopiaeconomy.53610/)

## Installatie
1. Download de source code of gebruik `git clone https://github.com/TheMelvinNL/MinetopiaEconomy`
2. Installeer [Gradle](https://gradle.org/).
3. Voer `gradle shadowJar` uit.
4. De jar staat nu in `\build\libs`.

## Doneer
Doneren kan via de volgende link: https://paypal.me/melvinsnijders

## Contributie
* Nieuwe features en bug fixes moeten gemaakt en getest worden in een nieuwe branch.
* Deze branches moeten altijd gemerged worden met development.
* Commits moeten gebruik maken van [GitMoji](https://gitmoji.carloscuesta.me/).

## License
MIT License

Copyright (c) 2020 Melvin Snijders and Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.