package org.epst.controlleurs;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("hls")
public class HlsResource {

    private static final String HLS_FOLDER = "C:\\Users\\Pierre\\Documents\\hls";

    @GET
    @Path("/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getHlsFile(@PathParam("fileName") String fileName) {
        File file = new File(HLS_FOLDER, fileName);

        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Fichier non trouvé").build();
        }

        try {
            return Response.ok(Files.readAllBytes(Paths.get(file.getAbsolutePath())))
                    .header("Content-Disposition", "inline; filename=" + fileName)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erreur serveur").build();
        }
    }

    @GET
    @Path("/lecteur")
    @Produces(MediaType.TEXT_HTML)
    public Response getHlsFile() {

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Live Streaming HLS</title>\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/video.js/7.10.2/video.min.js\"></script>\n" +
                "    <link href=\"https://cdnjs.cloudflare.com/ajax/libs/video.js/7.10.2/video-js.min.css\" rel=\"stylesheet\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <video id=\"videoPlayer\" class=\"video-js vjs-default-skin\" controls width=\"640\" height=\"360\">\n" +
                "        <source src=\"http://192.168.134.134:8080/hls/stream.m3u8\" type=\"application/x-mpegURL\">\n" +
                "    </video>\n" +
                "\n" +
                "    <script>\n" +
                "        var player = videojs('videoPlayer');\n" +
                "        player.play();\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>\n";


        return Response.ok(html).build();
    }

    @GET
    @Path("/lecteur_mpeg")
    @Produces(MediaType.TEXT_HTML)
    public Response lecteurMpeg() {

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Live Stream MPEG-DASH</title>\n" +
                "    <script src=\"https://cdn.dashjs.org/latest/dash.all.min.js\"></script>\n" +
                "    <style>\n" +
                "        body { text-align: center; background: #111; color: white; font-family: Arial, sans-serif; }\n" +
                "        video { width: 80%; max-width: 800px; margin-top: 20px; border: 2px solid white; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "    <h1>\uD83D\uDD34 Live Stream MPEG-DASH</h1>\n" +
                "    <video id=\"videoPlayer\" controls autoplay></video>\n" +
                "\n" +
                "    <script>\n" +
                "        (function() {\n" +
                "            var url = \"http://192.168.134.134:8080/hls/output.mpd\"; // Remplace par ton URL DASH\n" +
                "            var video = document.querySelector(\"#videoPlayer\");\n" +
                "            var player = dashjs.MediaPlayer().create();\n" +
                "            player.initialize(video, url, true);\n" +
                "\n" +
                "            // Activer un mode faible latence\n" +
                "            player.updateSettings({\n" +
                "                streaming: {\n" +
                "                    lowLatencyEnabled: true,\n" +
                "                    liveDelay: 2, // Latence de 2 secondes\n" +
                "                    scheduleWhilePaused: false\n" +
                "                }\n" +
                "            });\n" +
                "        })();\n" +
                "    </script>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";


        return Response.ok(html).build();
    }


}
