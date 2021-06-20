import sun.dc.path.PathException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Objects;

public class FileManager {

    protected Path currentPath;

    public FileManager (String pathString) throws InvalidPathException, PathException {
        try {
            Path currentPath = Paths.get(pathString);
            currentPath.normalize();
            if (Files.exists(currentPath)){
                this.currentPath = currentPath;
            } else {
                throw new PathException();
            }
        } catch (InvalidPathException ex){
            throw new InvalidPathException(pathString,"Введённая строка не может быть преобразована в путь");
        } catch (PathException ex){
            throw new PathException("Системе не удаётся найти указанный путь.");
        }
    }

    public void dir() throws IOException {
        ArrayList<Path> names = new ArrayList<>();
        ArrayList<FileTime> lastModifiedTimes= new ArrayList<>();
        ArrayList<Long> sizes = new ArrayList<>();
        ArrayList<UserPrincipal> owners = new ArrayList<>();
        try{
            Files.walk(this.currentPath,1).forEach(x->names.add(x.getFileName()));
            Files.walk(this.currentPath,1).forEach(x-> {
                try {
                    lastModifiedTimes.add(Files.getLastModifiedTime(x));
                } catch (IOException e) {
                    lastModifiedTimes.add(null);
                }
            });
            Files.walk(this.currentPath,1).forEach(x->
            {
                try {
                    sizes.add(Files.size(x));
                } catch (IOException e) {
                    sizes.add(null);
                }
            });
            Files.walk(this.currentPath,1).forEach(x-> {
                try {
                    owners.add(Files.getOwner(x));
                } catch (IOException e) {
                    owners.add(null);
                }
            });
        } catch (IOException io){
            throw new IOException("Невозможно получить доступ к текущей директории.",io);
        }

        System.out.println("\nСодержимое папки " + this.currentPath.toString());

        String[] str;
        int maxOwner = owners.stream()
                    .filter(Objects::nonNull)
                    .max((a,b)->Integer.compare(a.toString().length(),b.toString().length()))
                    .orElse(null)
                    .toString()
                    .length();
        int maxSize = sizes.stream()
                    .filter(Objects::nonNull)
                    .max((a,b)->Integer.compare(a.toString().length(),b.toString().length()))
                    .toString()
                    .length();
        int maxName = names.stream()
                    .filter(Objects::nonNull)
                    .max((a,b)->Integer.compare(a.toString().length(),b.toString().length()))
                    .toString()
                    .length();
        for (int i = 1;i<names.size();i++){
            str = lastModifiedTimes.get(i).toString().split("-");
            System.out.print(str[0] + "." + str[1] + "." + str[2].substring(0,2) + " | " + str[2].substring(3,8) +
                               " | ");
            if (owners.get(i)!=null){
                System.out.print(owners.get(i).toString());
                for (int j = 0;j<(maxOwner - owners.get(i).toString().length());j++){
                    System.out.print(" ");
                }
            } else {
                for (int j = 0;j<maxOwner;j++){
                    System.out.print(" ");
                }
            }
            System.out.print(" | " + sizes.get(i));
            for (int j = 0;j<(maxSize - sizes.get(i).toString().length());j++){
                System.out.print(" ");
            }
            System.out.print(" | " + names.get(i));
            for (int j = 0;j<(maxName - names.get(i).toString().length());j++){
                System.out.print(" ");
            }
            System.out.println(" |");
        }
        int maxString = 3*3+2 + 15 + maxName + maxOwner + maxSize;
        int numberOfDirectory = 0;
        File file = new File(currentPath.toString());
        long weightOfFiles = 0;
        ArrayList<Path> paths = new ArrayList<>();
        Files.walk(this.currentPath,1).forEach(paths::add);
        for (int i = 1;i<paths.size();i++){
            if (Files.isDirectory(paths.get(i))){
                numberOfDirectory++;
            } else {
                weightOfFiles+=sizes.get(i);
            }
        }
        String inf1 = (names.size()-1-numberOfDirectory) + " файлов " + weightOfFiles + " байт";
        String inf2 = numberOfDirectory + " папок " + file.getFreeSpace() + " байт свободно";
        for (int i = 0;i<(maxString - inf1.length());i++){
            System.out.print(" ");
        }
        System.out.println(inf1);
        for (int i = 0;i<(maxString - inf2.length());i++){
            System.out.print(" ");
        }
        System.out.println(inf2);
    }

    public void cd(String str) throws InvalidPathException, PathException {
        Path newPath;
        try{
            newPath = Paths.get(str);
        } catch (InvalidPathException ex){
            throw new InvalidPathException(str,"Введённая строка не может быть преобразована в путь");
        }
        newPath.normalize();
        if (newPath.isAbsolute()){
            if (Files.exists(newPath)){
                if (Files.isDirectory(newPath)){
                    this.currentPath = newPath;
                } else {
                    throw new PathException("Неверно задано имя папки.");
                }
            } else {
                throw new PathException("Системе не удаётся найти указанный путь.");
            }
        } else {
            Path path = Paths.get(currentPath.toString() + "\\" + newPath).normalize();
            if (Files.exists(path)){
                if (Files.isDirectory(path)){
                    this.currentPath = path;
                } else {
                    throw new PathException("Неверно задано имя папки.");
                }
            } else {
                throw new PathException("Системе не удаётся найти указанный путь.");
            }
        }
    }

    public void openFile(String str) throws PathException, InvalidPathException, IOException {
        if (str.equals("")){
            try{
                Desktop.getDesktop().open(new File(currentPath.toString()));
            } catch (IOException io){
                throw new IOException("Невозможно открыть файл");
            }
        } else {
            Path newPath;
            try{
                newPath = Paths.get(str);
            } catch (InvalidPathException ex){
                throw new InvalidPathException(str,"Введённая строка не может быть преобразована в путь");
            }
            newPath.normalize();
            if (newPath.isAbsolute()){
                if (Files.exists(newPath)){
                    File file = new File(newPath.toString());
                    try{
                        Desktop.getDesktop().open(file);
                    } catch (IOException io){
                        throw new IOException("Невозможно открыть файл");
                    }
                } else {
                    throw new PathException("Системе не удаётся найти указанный путь.");
                }
            } else {
                Path path = Paths.get(currentPath.toString() + "\\" + newPath).normalize();
                if (Files.exists(path)){
                    File file = new File(path.toString());
                    try{
                        Desktop.getDesktop().open(file);
                    } catch (IOException io){
                        throw new IOException("Невозможно открыть файл");
                    }
                } else {
                    throw new PathException("Системе не удаётся найти указанный путь.");
                }
            }
        }
    }

    public void deleteFile(String str) throws NoSuchFileException, IOException, SecurityException, InvalidPathException, PathException {
        if (str.equals("")){
            try{
                Files.delete(currentPath);
                this.currentPath = currentPath.getParent();
            } catch (NoSuchFileException ex){
                throw new NoSuchFileException("Такого файла уже не существует.");
            } catch (IOException io){
                throw new IOException("Невозможно удалить файл");
            } catch (SecurityException ex){
                throw new SecurityException("Невозможно получить доступ к указанному файлу");
            }
        } else {
            Path newPath;
            try{
                newPath = Paths.get(str);
            } catch (InvalidPathException ex){
                throw new InvalidPathException(str,"Введённая строка не может быть преобразована в путь");
            }
            newPath.normalize();
            if (newPath.isAbsolute()){
                if (Files.exists(newPath)){
                    try{
                        Files.delete(newPath);
                        if (newPath.equals(currentPath)){
                            this.currentPath = currentPath.getParent();
                        }
                    } catch (NoSuchFileException ex){
                        throw new NoSuchFileException("Такого файла уже не существует.");
                    } catch (IOException io){
                        throw new IOException("Невозможно удалить файл");
                    } catch (SecurityException ex){
                        throw new SecurityException("Невозможно получить доступ к указанному файлу");
                    }
                } else {
                    throw new PathException("Системе не удаётся найти указанный путь.");
                }
            } else {
                Path path = Paths.get(currentPath.toString() + "\\" + newPath).normalize();
                if (Files.exists(path)){
                    try{
                        Files.delete(path);
                        if (path.equals(currentPath)){
                            this.currentPath = currentPath.getParent();
                        }
                    } catch (NoSuchFileException ex){
                        throw new NoSuchFileException("Такого файла уже не существует.");
                    } catch (IOException io){
                        throw new IOException("Невозможно удалить файл");
                    } catch (SecurityException ex){
                        throw new SecurityException("Невозможно получить доступ к указанному файлу");
                    }
                } else {
                    throw new PathException("Системе не удаётся найти указанный путь.");
                }
            }
        }
    }

    public Path getCurrentPath()throws InvalidPathException{
        return currentPath;
    }

    public void copyFile (String str, String s) throws PathException, IOException {
        Path outPath;
        if (str.equals("")) {
            outPath = Paths.get(currentPath.toString());
        } else {
            Path newPath;
            try {
                newPath = Paths.get(str);
            } catch (InvalidPathException ex) {
                throw new InvalidPathException(str, "Введённая строка не может быть преобразована в путь");
            }
            newPath.normalize();
            if (newPath.isAbsolute()) {
                if (Files.exists(newPath)) {
                    outPath = newPath;
                } else {
                    throw new PathException("Системе не удаётся найти указанный путь.");
                }
            } else {
                Path path = Paths.get(currentPath.toString() + "\\" + newPath).normalize();
                if (Files.exists(path)) {
                    outPath = path;
                } else {
                    throw new PathException("Системе не удаётся найти указанный путь.");
                }
            }
        }
        Path inPath;
        Path newPath;
        try {
            newPath = Paths.get(s);
        } catch (InvalidPathException ex) {
            throw new InvalidPathException(str, "Введённая строка не может быть преобразована в путь");
        }
        newPath.normalize();
        if (newPath.isAbsolute()) {
            if (Files.exists(newPath)) {
                if (Files.isDirectory(newPath)){
                    inPath = newPath;
                } else {
                    throw new PathException("Указанный путь не являются директорией");
                }
            } else {
                throw new PathException("Системе не удаётся найти указанный путь.");
            }
        } else {
            throw new PathException("Неверный путь.");
        }
        copy(outPath, inPath);
    }

    protected void copy(Path outPath, Path inPath) throws IOException, FileAlreadyExistsException, SecurityException {
        if (Files.isDirectory(outPath)){
            ArrayList<Path> list = new ArrayList<>();
            Files.walk(outPath, 1).forEach(list::add);
            try{
                Files.createDirectory(Paths.get(inPath.toString() + "\\" + outPath.getFileName()));
            } catch (FileAlreadyExistsException ex){
                throw new FileAlreadyExistsException("Данный файл уже существует");
            } catch (SecurityException se){
                throw new SecurityException("Нет доступа к указанному пути");
            } catch (IOException ioe){
                throw new IOException("Ошибка ввода/вывода");
            }
            Path newPath = Paths.get(inPath.toString() +"\\" + outPath.getFileName());
            for (int i = 1;i<list.size();i++){
                copy (list.get(i),newPath);
            }
        } else {
            try{
                Files.copy(outPath, Paths.get(inPath.toString() + "\\" + outPath.getFileName()));
            } catch (FileAlreadyExistsException ex){
                throw new FileAlreadyExistsException("Данный файл уже существует");
            } catch (SecurityException se){
                throw new SecurityException("Нет доступа к указанному пути");
            } catch (IOException ioe){
                throw new IOException("Ошибка ввода/вывода");
            }
        }
    }
}
