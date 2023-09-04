# Require the json package to read package.json
require "json"
# Read package.json to get some metadata about our package
package = JSON.parse(File.read(File.join(__dir__, "./package.json")))
# Define the configuration of the package
Pod::Spec.new do |s|
  # Name and version are taken directly from the package.json
  s.name            = package["name"]
  s.version         = package["version"]
  # Optionally you can add other fields in package.json like
  # description, homepage, license, authors etc.
  # to keep it simple, I added them as inline strings
  # feel free to edit them however you want!
  s.homepage        = "https://reactnativecrossroads.com"
  s.summary         = "Sample Contacts module"
  s.license         = "MIT"
  s.platforms       = { :ios => min_ios_version_supported }
  s.author          = "Jacek Pudysz"
  s.source          = { :git => package["repository"], :tag => "#{s.version}" }
  # Define the source files extension that we want to recognize
  # Soon, we'll create the ios folder with our module definition
  s.source_files    = "ios/*.{h,m,mm}"
  # This part installs all required dependencies like Fabric, React-Core, etc.
  install_modules_dependencies(s)
end
