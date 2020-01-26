package bt.types.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.WatchKey;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import bt.types.files.evnt.FileContentEvent;
import bt.types.files.evnt.FileCreateEvent;
import bt.types.files.evnt.FileDeleteEvent;
import bt.types.files.evnt.FileModifyEvent;
import bt.types.files.evnt.FileObserverEvent;
import bt.utils.log.Logger;

/**
 * An extension of {@link FileObserver} which offers additional functions to receive events for changes in file sizes
 * plus the changed textual content of files.
 *
 * <p>
 * Determination of changed textual content only works if the content was added to the very end of the file as this
 * class does only compare file sizes and creates a substring instead of comparing the true text values.
 * </p>
 *
 * @author &#8904
 */
public class FileContentObserver extends FileObserver
{
    private Map<File, Long> fileSizes;

    public FileContentObserver()
    {
        super();
        this.fileSizes = new HashMap<>();
    }

    /**
     * Subscribes to content change events fired by this instance.
     *
     * <p>
     * Events of this type are fired additionally to {@link FileModifyEvent}s for text based files whichs size
     * increased. The added text can only be correctly determined if it was added to the very end of the file, because
     * this class does only compare file sizes and creates a substring instead of comparing the true text values.
     * </p>
     *
     * <p>
     * This event is NOT fired if the file was modified but the size of the content remains the same.
     * </p>
     *
     * @param consumer
     *            The {@link Consumer} that will receive fired events.
     */
    public void subscribeContent(Consumer<FileContentEvent> consumer)
    {
        this.eventDispatcher.subscribeTo(FileContentEvent.class, consumer);
    }

    @Override
    protected void setFile(WatchKey key, File file)
    {
        super.setFile(key, file);

        if (file.isDirectory())
        {
            Stream.of(file.listFiles())
                  .filter(File::isFile)
                  .forEach(f ->
                  {
                      addFile(f);
                  });
        }
        else
        {
            addFile(file);
        }
    }

    private void addFile(File file)
    {
        long length = 0;
        try
        {
            length = Files.readString(file.toPath()).length();

        }
        catch (IOException e)
        {
            try
            {
                length = Files.size(file.toPath());
            }
            catch (IOException e1)
            {
                Logger.global().print(e1);
            }
        }

        this.fileSizes.put(file, length);
    }

    @Override
    protected void dispatchEvent(FileObserverEvent e)
    {
        super.dispatchEvent(e);

        File file = e.fullContext().toFile();

        if (e instanceof FileCreateEvent)
        {
            addFile(file);
        }
        else if (e instanceof FileDeleteEvent)
        {
            this.fileSizes.remove(file);
        }
        else if (e instanceof FileModifyEvent)
        {
            String content = null;
            long nonTextLength = 0;

            try
            {
                content = Files.readString(e.fullContext());
            }
            catch (IOException e1)
            {
                // non text based files cant be read to string
                try
                {
                    nonTextLength = Files.size(e.fullContext());
                }
                catch (IOException e2)
                {
                    Logger.global().print(e1);
                }
            }

            long oldSize = this.fileSizes.get(file);
            long newSize = content != null ? content.length() : nonTextLength;
            this.fileSizes.put(file, newSize);

            if (oldSize != newSize)
            {
                var event = new FileContentEvent(e, e.observedPath());
                event.setOldSize(oldSize);
                event.setNewSize(newSize);

                if (content != null && newSize > oldSize)
                {
                    event.setAddedContent(content.substring(Math.toIntExact(oldSize)));
                }

                this.eventDispatcher.dispatch(event);
            }
        }
    }
}