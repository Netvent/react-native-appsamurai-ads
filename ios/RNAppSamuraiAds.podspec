
Pod::Spec.new do |s|
  s.name         = "RNAppSamuraiAds"
  s.version      = "1.0.0"
  s.summary      = "RNAppSamuraiAds"
  s.description  = <<-DESC
                  RNAppSamuraiAds
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNAppSamuraiAds.git", :tag => "master" }
  s.source_files  = "RNAppSamuraiAds/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  