package bt.types.files.evnt;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @author &#8904
 *
 */
public class FileCreateEvent extends FileObserverEvent
{
    public FileCreateEvent(WatchEvent<Path> e)
    {
        super(e);
    }

    public FileCreateEvent(WatchEvent<Path> e, Path observerPath)
    {
        super(e, observerPath);
    }
}