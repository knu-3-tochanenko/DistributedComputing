# How to run this project

### Install MPJ on your Windows 10 PC

1. You need to download latest version of MPJ from [SourceForge](https://sourceforge.net/projects/mpjexpress/files/releases/). This project uses v44, so you can quickly download it [here (download will start automatically)](https://sourceforge.net/projects/mpjexpress/files/releases/mpj-v0_44.zip/download).
2. Unzip downloaded archive to your libraries folder which **DOES NOT** contain any spaces or special characters. `mpj` folder located on `C:\tools\libs\mpj` on my computer, so we will use this pass as an example.
3. Open system variables window. You can easily access this window by pressing `Windows` key (assuming you are using Windows 10), type `variables` and press `Enter`.
4. Go to `Environment Variables...`.
5. Create new variable with name `MPJ_HOME` and value `C:\utils\libs\mpj` for your user *OR* System by pressing on `New...`. Change value to path where you unzipped `mpj` folder.
6. Select Path variable and press on `New`. Then paste path to `mpj\bin` folder. It is `C:\utils\libs\mpj\bin` for me and can be different for you.
7. Click `OK -> OK -> OK`.
8. Reboot your PC.

> I added and changed variables for my user so I don't mess with other users variables. If you use your PC by yourself feel free to add and change system variables.   


### Setup project

1. Press on `Ctrl + Alt + Shift + S` or go to `File -> Project Structure...`.
2. Under `Project settings.Properties` select `Libraries`.
3. Add `New Project Library` by tapping on plus sign in the second column.
4. Select `Java` from dropdown and select `mpi.jar` and `mpj.jar` files from `mpj\lib`. It is located in `C:\utils\libs\mpj\lib` for me.

> If you accidentally added only one file (`mpi.jar` or `mpj.jar`) tap on plus sign above `Classes` dropdown.

### Run project

I used 4 configurations to automate running with 1 Core, 4 Cores and creating report in HTML table using only one button. You will need to create these Run configurations:

* Remove Files
* 1 Core
* 4 Cores
* Generate Table

To create **any** of these configurations, you will need to perform these actions:

1. Open `Lab 8` folder from IntelliJ IDEA as project.
2. Press on `Alt + Shift + F10 -> Edit Configurations...` or `Run -> Edit Configurations...`.
3. `Add New Configuration` by tapping on plus sign. Select `Application`.
4. Name this configuration however you want.

Now, create first configuration named `Remove Files`:
1. Create new run configuration.
2. Give it a name `Remove Files`.
3. Select `Main Class` as `logger.FilesCleaner`.
4. Make sure that `Build` action is present in `Before launch` section. If not, click on `+` sign and add action named `Build`.

The second configuration will launch application in single-core mode:

1. Create new run configuration.
2. Give it a name `1 Core`.
3. Setup this configuration using these parameters:
   * **Main class**: `Main` from `default package`;
   * **VM options**: `-jar $MPJ_HOME$\lib\starter.jar Main -np 1`;
   * Leave **Working directory** as default;
   * **Environment variables**: `MPJ_HOME=C:\utils\libs\mpj`. Remember to change path to `mpj` folder where your `mpj` folder is located;
   * Leave **Use classpath of module** as default.
   * Select `Build` in `Before launch` section and click on `-` to remove it. Then click on `+` sign in `Before launch` section, press on `Run Another Configuration` and select `Remove Files`.

The third configuration will launch application in multi-core (4) mode:

> You can copy previous configuration by selecting it on the left of the window and pressing `Ctrl+D`. Then change differing fields instead of re-typing them. Only `VM options` and `Before launch` will be changed

1. Create new run configuration.
2. Give it a name `4 Core`.
3. Setup this configuration using these parameters:
   * **Main class**: `Main` from `default package`;
   * **VM options**: `-jar $MPJ_HOME$\lib\starter.jar Main -np 4`;
   * Leave **Working directory** as default;
   * **Environment variables**: `MPJ_HOME=C:\utils\libs\mpj`. Remember to change path to `mpj` folder where your `mpj` folder is located;
   * Leave **Use classpath of module** as default.
   * Select `Build` in `Before launch` section and click on `-` to remove it. Then click on `+` sign in `Before launch` section, press on `Run Another Configuration` and select `1 Core`.

The last configuration will create `table.html` file based on results of previous configurations:

1. Create new run configuration.
2. Give it a name `Generate Table`.
3. Select `Main Class` as `logger.TableCreator`.
4. Select `Build` in `Before launch` section and click on `-` to remove it. Then click on `+` sign in `Before launch` section, press on `Run Another Configuration` and select `4 Core`.

Click on `Apply -> Close` to save these configurations or `Run` to run it immediately!

Select `Generate Table` configuration in configurations dropdown and press on green triangle **OR** press `Alt + Shift + F10`, select `Generate Table` and press `Enter` **OR** press `Ctrl + Ctrl` and start typing `Generate Table` and press `Enter` **OR** press `Shift + Shift` and start typing `Generate Table`, select `Generate Table` in the list and press `Enter`.

After first launch, you can press `Shift + F10` to run `Generate Table` again.
