<!-- Hide script from old browsers

banImages = new Array("grfx/banner/ban1.jpg",
                      "grfx/banner/ban2.jpg",
                      "grfx/banner/ban3.jpg",
                      "grfx/banner/ban4.jpg");
imgPtr = 0;
imgCnt = banImages.length;

function rotate() {

   if (document.images) {

      if (document.imgBanner.complete) {
         imgPtr++;
         if (imgPtr == imgCnt) imgPtr = 0;
         document.imgBanner.src=banImages[imgPtr];
      }

      setTimeout("rotate()", 3000);

   }

}

// End hiding script from old browsers -->
