package in.ajm.sb.helper.recorder;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.List;

import in.ajm.sb.helper.LoggerCustom;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 8/25/13
 */
final class Mp4ParserWrapper {

    private static final String TAG = "Mp4ParserWrapper";

    private static final int FILE_BUFFER_SIZE = 1024;

    private Mp4ParserWrapper() {
    }

    /**
     * Appends mp4 audios/videos: {@code anotherFileName} to {@code mainFileName}.
     */
    public static boolean append(String mainFileName, String anotherFileName) {
        boolean rvalue = false;
        try {
            File targetFile = new File(mainFileName);
            File anotherFile = new File(anotherFileName);
            if (targetFile.exists() && targetFile.length() > 0) {
                String tmpFileName = mainFileName + ".tmp";
                append(mainFileName, anotherFileName, tmpFileName);
                copyFile(tmpFileName, mainFileName);
                rvalue = anotherFile.delete() && new File(tmpFileName).delete();
            } else {
                if (targetFile.exists()) {
                    copyFile(anotherFileName, mainFileName);
                    rvalue = anotherFile.delete();
                } else {
                    if (targetFile.createNewFile()) {
                        copyFile(anotherFileName, mainFileName);
                        rvalue = anotherFile.delete();
                    } else {
                        rvalue = false;
                    }
                }
//                if ((targetFile.getParentFile().mkdirs()
//                        && targetFile.createNewFile())
//                        || targetFile.exists()) {
//                    copyFile(anotherFileName, mainFileName);
//                    rvalue = anotherFile.delete();
//                } else {
//                    rvalue = false;
//                }
            }
        } catch (IOException e) {
            LoggerCustom.printStackTrace(e);
        }
        return rvalue;
    }


    private static void copyFile(final String from, final String destination)
            throws IOException {
        FileInputStream in = new FileInputStream(from);
        FileOutputStream out = new FileOutputStream(destination);
        copy(in, out);
        in.close();
        out.close();
    }

    private static void copy(FileInputStream in, FileOutputStream out) throws IOException {
        byte[] buf = new byte[FILE_BUFFER_SIZE];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
    }

    private static void append(
            final String firstFile,
            final String secondFile,
            final String newFile) throws IOException {
        final Movie movieA = MovieCreator.build(new FileDataSourceImpl(secondFile));
        final Movie movieB = MovieCreator.build(new FileDataSourceImpl(firstFile));

        final Movie finalMovie = new Movie();

        final List<Track> movieOneTracks = movieA.getTracks();
        final List<Track> movieTwoTracks = movieB.getTracks();

        for (int i = 0; i < movieOneTracks.size() || i < movieTwoTracks.size(); ++i) {
            finalMovie.addTrack(new AppendTrack(movieTwoTracks.get(i), movieOneTracks.get(i)));
        }

        final Container container = new DefaultMp4Builder().build(finalMovie);

        final FileOutputStream fos = new FileOutputStream(new File(newFile));
        final WritableByteChannel bb = Channels.newChannel(fos);
        container.writeContainer(bb);
        fos.close();
    }

}
