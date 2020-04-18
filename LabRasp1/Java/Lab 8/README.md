# How to run this project

### Install MPJ on your Windows 10 PC

1. You need to download latest version of MPJ from [SourceForge](https://sourceforge.net/projects/mpjexpress/files/releases/). This project uses v44, so you can quickly download it [here (download will start automatically)](https://sourceforge.net/projects/mpjexpress/files/releases/mpj-v0_44.zip/download).
2. Unzip downloaded archive to your libraries folder which **DOES NOT** contain any spaces or special characters. `mpj` folder located on `C:\tools\libs\mpj` on my computer, so we will use this pass as an example.
3. Open system variables window. You can easily access this window by pressing `Windows` key (assuming you are using Windows 10), type `variables` and press `Enter`.
4. Go to `Environment Variables...`.
5. Create new variable with name `MPJ_HOME` and value `C:\tools\libs\mpj` for your user *OR* System by pressing on `New...`. Change value to path where you unzipped `mpj` folder.
6. Select Path variable and press on `New`. Then paste path to `mpj\bin` folder. It is `C:\tools\libs\mpj\bin` for me and can be different for you.
7. Click `OK -> OK -> OK`.
8. Reboot your PC.

> I added and changed variables for my user so I don't mess with other users variables. If you use your PC by yourself feel free to add and change system variables.   


### Setup project

1. Press on `Ctrl + Alt + Shift + S` or go to `File -> Project Structure...`.
2. Under `Project settings.Properties` select `Libraries`.
3. Add `New Project Library` by tapping on plus sign in the second column.
4. Select `Java` from dropdown and select `mpi.jar` and `mpj.jar` files from `mpj\lib`. It is located in `C:\tools\libs\mpj\lib` for me.

> If you accidentally added only one file (`mpi.jar` or `mpj.jar`) tap on plus sign above `Classes` dropdown.

### Run project

1. Open `Lab 8` folder from IntelliJ IDEA as project.
2. Press on `Alt + Shift + F10 -> Edit Configurations...` or `Run -> Edit Configurations...`.
3. `Add New Configuration` by tapping on plus sign. Select `Application`.
4. Name this configuration however you want. I named it `Run MPJ Project`.
5. Now we need to setup this configuration:
   * **Main class**: `Main` from `default package`;
   * **VM options**: `-jar $MPJ_HOME$\lib\starter.jar Main -np 5`;
   * Leave **Working directory** as default;
   * **Environment variables**: `MPJ_HOME=C:\tools\libs\mpj`. Remember to change path to `mpj` folder where your `mpj` folder is located;
   * Leave **Use classpath of module** as default.
6. Click on `Apply -> Close` to save this configuration or `Run` to run it immediately!

> To run this configuration tap on `Shift + F10` and select yor configuration or `Run -> Run '<configuration name>'`.