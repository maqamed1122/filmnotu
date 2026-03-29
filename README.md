# filmnot 🎬

Film və serialları izləyin, qeyd edin, qiymətləndirin.

## Xüsusiyyətlər

- 🔥 Trend, populyar, top-rated film/seriallar
- 🔍 TMDB axtarış
- 🎬 YouTube fragman oynatma
- ⭐ IMDb keçidi
- 📋 İzləmə siyahısı (watchlist)
- ✅ İzlənildi/İzlənilmədi qeyd
- ❤️ Sevimlilər
- ⭐ Şəxsi reytinq
- 📊 Statistika

## Qurulum (GitHub Actions)

1. Bu reponu GitHub-a push edin
2. `Settings → Secrets → Actions` bölməsinə keçin
3. `TMDB_API_KEY` secret əlavə edin (dəyər: `370e43d715d230cce90a5ae69d40cce4`)
4. `Actions` tabına keçin → workflow avtomatik başlayacaq
5. APK `Artifacts` bölməsindən yüklənə bilər

## Texnologiyalar

- Kotlin + Jetpack Compose
- Hilt (DI)
- Room (lokal DB)
- Retrofit (TMDB API)
- Coil (şəkil yükləmə)
- YouTube Player
- Navigation Compose
