# IoT_Error_Scanner_Grp1
University Project, scans QR Code and sends it to backend.
1. Receives easy solution for problem at Fischertechnik Machine, if not done with this solution, send new request to backend.
2. Request shall then be handled at Prisma.

## Dependencies in build.gradle:
    // google lib for scanning barcodes / qr codes
    implementation 'com.google.android.gms:play-services-vision:20.0.0'

    // lib for http requests
    implementation("com.squareup.okhttp3:okhttp:4.6.0")

### See compiled APK under releases in this repo
