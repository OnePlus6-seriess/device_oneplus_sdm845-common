/*
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.awaken.device.DeviceSettings;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.NullPointerException;
import java.lang.SecurityException;

public final class FileUtils {
    private static final String TAG = "FileUtils";

    private FileUtils() {
        // This class is not supposed to be instantiated
    }

    /**
     * Reads the first line of text from the given file.
     * Reference {@link BufferedReader#readLine()} for clarification on what a line is
     *
     * @return the read line contents, or null on failure
     */
    public static String readOneLine(String fileName) {
        String line = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName), 512);
            line = reader.readLine();
        } catch (FileNotFoundException e) {
            Log.w(TAG, "No such file " + fileName + " for reading", e);
        } catch (IOException e) {
            Log.e(TAG, "Could not read from file " + fileName, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // Ignored, not much we can do anyway
            }
        }

        return line;
    }

    /**
     * Writes the given value into the given file
     *
     * @return true on success, false on failure
     */
    public static boolean writeLine(String fileName, String value) {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(value);
        } catch (FileNotFoundException e) {
            Log.w(TAG, "No such file " + fileName + " for writing", e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Could not write to file " + fileName, e);
            return false;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                // Ignored, not much we can do anyway
            }
        }

        return true;
    }

    /**
     * Checks whether the given file exists
     *
     * @return true if exists, false if not
     */
    public static boolean fileExists(String fileName) {
        final File file = new File(fileName);
        return file.exists();
    }

    /**
     * Checks whether the given file is readable
     *
     * @return true if readable, false if not
     */
    public static boolean isFileReadable(String fileName) {
        final File file = new File(fileName);
        return file.exists() && file.canRead();
    }

    /**
     * Checks whether the given file is writable
     *
     * @return true if writable, false if not
     */
    public static boolean isFileWritable(String fileName) {
        final File file = new File(fileName);
        return file.exists() && file.canWrite();
    }

    /**
     * Deletes an existing file
     *
     * @return true if the delete was successful, false if not
     */
    public static boolean delete(String fileName) {
        final File file = new File(fileName);
        boolean ok = false;
        try {
            ok = file.delete();
        } catch (SecurityException e) {
            Log.w(TAG, "SecurityException trying to delete " + fileName, e);
        }
        return ok;
    }

    /**
     * Renames an existing file
     *
     * @return true if the rename was successful, false if not
     */
    public static boolean rename(String srcPath, String dstPath) {
        final File srcFile = new File(srcPath);
        final File dstFile = new File(dstPath);
        boolean ok = false;
        try {
            ok = srcFile.renameTo(dstFile);
        } catch (SecurityException e) {
            Log.w(TAG, "SecurityException trying to rename " + srcPath + " to " + dstPath, e);
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException trying to rename " + srcPath + " to " + dstPath, e);
        }
        return ok;
 * limitations under the License
 */

package com.awaken.device.DeviceSettings;

import android.os.SystemProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

class FileUtils {

    static boolean fileWritable(String filename) {
        return fileExists(filename) && new File(filename).canWrite();
    }

    private static boolean fileExists(String filename) {
        if (filename == null) {
            return false;
        }
        return new File(filename).exists();
    }

    static void setValue(String path, int value) {
        if (fileWritable(path)) {
            if (path == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File(path));
                fos.write(Integer.toString(value).getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void setValue(String path, boolean value) {
        if (fileWritable(path)) {
            if (path == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File(path));
                fos.write((value ? "1" : "0").getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void setValue(String path, double value) {
        if (fileWritable(path)) {
            if (path == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File(path));
                fos.write(Long.toString(Math.round(value)).getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void setValue(String path, String value) {
        if (fileWritable(path)) {
            if (path == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File(path));
                fos.write(value.getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static String readLine(String filename) {
        if (filename == null) {
            return null;
        }
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(filename), 1024);
            line = br.readLine();
        } catch (IOException e) {
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return line;
    }

    static boolean getFileValueAsBoolean(String filename, boolean defValue) {
        String fileValue = readLine(filename);
        if (fileValue != null) {
            return !fileValue.equals("0");
        }
        return defValue;
    }

    static boolean getFilesValueAsBoolean(String filename, boolean defValue) {
        String fileValue = readLine(filename);
        if (fileValue != null) {
            return !fileValue.equals("N");
        }
        return defValue;
    }

    static void setProp(String prop, boolean value) {
        if (value) {
            SystemProperties.set(prop, "1");
        } else {
            SystemProperties.set(prop, "0");
        }
    }

    static boolean getProp(String prop, boolean defaultValue) {
        return SystemProperties.getBoolean(prop, defaultValue);
    }

    static void setStringProp(String prop, String value) {
        SystemProperties.set(prop, value);
    }

    static String getStringProp(String prop, String defaultValue) {
        return SystemProperties.get(prop, defaultValue);
    }

    static void setintProp(String prop, int value) {
        SystemProperties.set(prop, String.valueOf(value));
    }

    static int getintProp(String prop, int defaultValue) {
        return SystemProperties.getInt(prop, defaultValue);
    }
}
