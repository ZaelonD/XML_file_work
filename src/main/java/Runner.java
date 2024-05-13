import errors.FunctionSyntaxErrorException;
import errors.InputDataException;
import sync.SynchronizationService;
import unload.UnloadService;

import static log.MyLogger.LOGGER;

public class Runner {
    public static void main(String[] args) {
        if (args.length != 2) {
            LOGGER.error("Invalid number of input parameters");
            throw new InputDataException("Invalid number of input parameters");
        }
        switch (args[0]) {
            case "unload":
                UnloadService unloadService = new UnloadService(args[1]);
                unloadService.unload();
                break;
            case "sync":
                SynchronizationService syncService = new SynchronizationService(args[1]);
                syncService.sync();
                break;
            default:
                LOGGER.error("There was an error in the spelling of the called function. Function " + args[0] + " does not exist");
                throw new FunctionSyntaxErrorException("There was an error in the spelling of the called function. Function " + args[0] + " does not exist");
        }
    }
}
