package org.ucnj.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
  * copy or download a file.
  *
  * @author  copyright (c) 1999 Roedy Green of Canadian Mind Products
  * may be copied and used freely for any purpose but military.
  *
  * Roedy Green
  * Canadian Mind Products
  * 5317 Barker Avenue
  * Burnaby, BC Canada V5H 2N6
  * tel: (604) 435-3052
  * mailto:roedy@mindprod.com
  * http://mindprod.com
  *
  * version 1.0 1999 Sept 5
  *        - implement download and copy.
  *        - use of readBlocking to replace readFully and read.
  *
  * version 1.1 1999 october 5
  *        - implement upload with PUT, and a status check.
  */
public class FileTransfer {

   static final boolean DEBUGGING = false;

   static final int BUFFSIZE = 63*1024;

   private int buffSize;

   /**
     * constructor
     */
   public FileTransfer () {
      this.buffSize = BUFFSIZE;
   }
   /**
     * constructor
     *
     * @param buffSize how big the i/o chunks are to copy files.
     */
   public FileTransfer (int buffSize) {
      if ( buffSize < 512 ) buffSize = 512;
      this.buffSize = buffSize;
   }
   /**
     * Copy a file from one spot on hard disk to another.
     *
     * @param source file to copy on local hard disk.
     *
     * @param target new file to be created on local hard disk.
     *
     * @return true if the copy was successful.
     */
   public boolean copy (java.io.File source, java.io.File target) {
      FileInputStream is = null;
      FileOutputStream os = null;

      try {
         // O P E N
         is = new FileInputStream(source);
         os= new FileOutputStream(target);

         // C O P Y  S O U R C E  T O  T A R G E T
         long fileLength = source.length();
         boolean success = copy(is, os, fileLength);
         if ( ! success ) {
            return false;
         }
         // C L O S E
         if ( is != null ) is.close();
         if ( os != null ) os.close();

      } catch ( IOException e ) {
         return false;
      }
      // all was ok
      return true;
   } // end copy
   /**
    * Copy an InputStream to an OutputStream, until EOF.
    * Use only when you don't know the length.
    *
    * @param source InputStream, left open.
    *
    * @param target OutputStream, left open.
    *
    * @return true if the copy was successful.
    */
   public boolean copy (java.io.InputStream source,
                        java.io.OutputStream target) {
      try {

         // R E A D / W R I T E by chunks

         int chunkSize = buffSize;
         // code will work even when chunkSize = 0 or chunks = 0;
         // Even for small files, we allocate a big buffer, since we
         // don't know the size ahead of time.
         byte[] ba = new byte[chunkSize];

         // keep reading till hit eof

         while ( true ) {

            int bytesRead = readBlocking (source, ba, 0, chunkSize);
            if ( DEBUGGING ) {
               System.out.println(bytesRead);
            }
            if ( bytesRead > 0 ) {
               target.write(ba,
                            0 /* offset in ba */,
                            bytesRead /* bytes to write */);
            } else {
               break; // hit eof
            }
         } // end while

         // C L O S E, done by caller if wanted.


      } catch ( IOException e ) {
         return false;
      }

      // all was ok
      return true;
   } // end copy
   /**
    * Copy an InputStream to an OutputStream.
    *
    * @param source InputStream, left open.
    *
    * @param target OutputStream, left open.
    *
    * @param length how many bytes to copy.
    *
    * @return true if the copy was successful.
    */
   public boolean copy (java.io.InputStream source,
                        java.io.OutputStream target,
                        long length) {
      try {

         // R E A D / W R I T E by chunks

         int chunkSize = (int)Math.min( buffSize, length );
         long chunks = length / chunkSize;
         int lastChunkSize = (int) (length % chunkSize);
         // code will work even when chunkSize = 0 or chunks = 0;
         byte[] ba = new byte[chunkSize];

         for ( long i=0; i<chunks; i++ ) {
            int bytesRead = readBlocking(source,
                                         ba,
                                         0,
                                         chunkSize);
            if ( bytesRead != chunkSize ) {
               throw new IOException();
            }
            target.write(ba);
         } // end for

         // R E A D / W R I T E last chunk, if any
         if ( lastChunkSize > 0 ) {
            int bytesRead = readBlocking(source,
                                         ba,
                                         0 /* offset in ba */,
                                         lastChunkSize);
            if ( bytesRead != lastChunkSize ) {
               throw new IOException();
            }
            target.write(ba,
                         0 /* offset in ba */,
                         lastChunkSize /* bytes to write */);
         } // end if

         // C L O S E, done by caller if wanted.


      } catch ( IOException e ) {
         return false;
      }

      // all was ok
      return true;
   } // end copy
   /**
    * Copy a file from a resource in the jar file to a local file on hard disk.
    *
    * @param source resource as stream e.g. this.class.getResourceAsStream("lyrics.exe");
    *
    * @param target new file to be created on local hard disk.
    *
    * @return true if the copy was successful.
    */
   public boolean download (InputStream source, java.io.File target) {
      FileOutputStream os = null;
      InputStream is = source;
      try {

         // O P E N  T A R G E T
         os = new FileOutputStream(target);

         // C O P Y  S O U R C E  T O  T A R G E T
         boolean success;
         success = copy(source, os);
         if ( ! success ) {
            return false;
         }

         // C L O S E
         if ( is != null ) is.close();
         if ( os != null ) os.close();

      } catch ( IOException e ) {
         return false;
      }

      // all was ok
      return true;
   } // end download
   /**
     * Copy a file from a remote URL to a local file on hard disk.
     *
     * @param source remote URL to copy. e.g.
     * new URL("http://www.billabong.com:80/songs/lyrics.txt")
     *
     * @param target new file to be created on local hard disk.
     *
     * @return true if the copy was successful.
     */
   public boolean download (java.net.URL source, java.io.File target) {
      URLConnection urlc = null;
      InputStream is = null;
      FileOutputStream os = null;
      try {

         // O P E N  S O U R C E
         urlc = source.openConnection();
         urlc.setAllowUserInteraction(false);
         urlc.setDoInput(true);
         urlc.setDoOutput(false);
         urlc.setUseCaches(false);
         urlc.connect();
         long length = urlc.getContentLength(); // -1 if not available
         if ( DEBUGGING && length < 0 ) {
            System.out.println("ContentLength not available");
         }
         is = urlc.getInputStream();

         // O P E N  T A R G E T
         os = new FileOutputStream(target);

         // C O P Y  S O U R C E  T O  T A R G E T
         boolean success;
         if ( length < 0 ) {
            success = copy(is, os);
         } else {
            success = copy(is, os, length);
         }
         if ( ! success ) {
            return false;
         }

         // C L O S E
         if ( is != null ) is.close();
         if ( os != null ) os.close();


      } catch ( IOException e ) {
         return false;
      }

      // all was ok
      return true;
   } // end download
   /**
     * Test driver
     */
   public static void main (String[] args) {

      if ( DEBUGGING ) {
         FileTransfer ft = new FileTransfer();
         System.out.println(ft.copy(new File("D:\\mindprod\\index.html"), new File("C:\\temp\\a.txt")));
         try {
            System.out.println(ft.download(new URL("http://mindprod.com/index.html"), new File("C:\\temp\\b.txt")));
         } 
         catch ( java.net.MalformedURLException e ) {
            System.out.println("bad download url");
         }

         try {
            System.out.println(ft.upload( new File("D:\\mindprod\\index.html"), new URL("http://mindprod.com/uploads/c.html")));
         } 
         catch ( java.net.MalformedURLException e ) {
            System.out.println("bad upload url");
         }


      } // end if debugging
   }  // end main 
   /**
   * Reads exactly len bytes from the input stream
   * into the byte array. This method reads repeatedly from the
   * underlying stream until all the bytes are read.
   * InputStream.read is often documented to block like this, but in actuality it
   * does not always do so, and returns early with just a few bytes.
   * readBlockiyng blocks until all the bytes are read,
   * the end of the stream is detected,
   * or an exception is thrown. You will always get as many bytes as you
   * asked for unless you get an eof or other exception.
   * Unlike readFully, you find out how many bytes you did get.
   *
   * @param b the buffer into which the data is read.
   * @param off the start offset of the data in the array,
   * not offset into the file!
   * @param len the number of bytes to read.
   * @return number of bytes actually read.
   * @exception IOException if an I/O error occurs.
   *
   */
   public static final int readBlocking (InputStream in, byte b[], int off, int len) throws IOException
   {
      int totalBytesRead = 0;
      while ( totalBytesRead < len ) {
         int bytesRead = in.read(b, off + totalBytesRead, len - totalBytesRead);
         if ( bytesRead < 0 ) {
            break;
         }
         totalBytesRead += bytesRead;
      }
      return totalBytesRead;
   } // end readBlocking
   /**
    * Copy a file from a local hard disk to a remote URL.
    * This simulates the HTML PUT upload command.
    * Unfortunately, most servers do not support it, or refuse it.
    *
    * @param source  existing  file to be copied on local hard disk.
    *
    * @param target remote URL to copy to. e.g.
    * new URL("http://www.billabong.com:80/songs/lyrics.txt")
    *
    * @return true if the copy was successful.
    */
   public boolean upload ( java.io.File source, java.net.URL target) {
      FileInputStream is = null;
      OutputStream os = null;
      HttpURLConnection urlc = null;
      try {

         // O P E N  S O U R C E
         is = new FileInputStream(source);
         long length = source.length();

         // O P E N  T A R G E T
         // must have file write/create permission on remote host

         // Generate a HTTP CGI POST command
         urlc = (HttpURLConnection) target.openConnection();
         urlc.setAllowUserInteraction(false);
         urlc.setDoInput(true);
         urlc.setDoOutput(true);
         urlc.setUseCaches(false);
         urlc.setRequestProperty("Content-type", "application/octet-stream");
         urlc.setRequestProperty("Content-length", Long.toString(length));
         urlc.setRequestMethod("PUT");
         urlc.connect();
         os = urlc.getOutputStream();

         // C O P Y  S O U R C E  T O  T A R G E T
         boolean success = copy(is, os, length);
         if ( ! success ) {
            return false;
         }

         int statusCode = urlc.getResponseCode();

         // C L O S E
         if ( is != null ) is.close();
         if ( os != null ) os.close();
         if ( urlc != null ) urlc.disconnect();

         return statusCode == 0;

      } catch ( IOException e ) {
         return false;
      }
   } // end upload
} // end class FileTransfer
