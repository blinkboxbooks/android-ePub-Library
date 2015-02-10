# -*- mode: ruby -*-
# vi: set ft=ruby :
#
# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  # All Vagrant configuration is done here. The most common configuration
  # options are documented and commented below. For a complete reference,
  # please see the online documentation at vagrantup.com.

  # Every Vagrant virtual environment requires a box to build off of.
  config.vm.box = "android-dev-epub-library-1410115779"

  # The url from where the 'config.vm.box' box will be fetched if it
  # doesn't already exist on the user's system.
  config.vm.box_url = "http://kernick.blinkbox.local/Android/android-dev-1410115779.box"

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # config.vm.network :forwarded_port, guest: 80, host: 8080

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  # config.vm.network :private_network, ip: "192.168.33.10"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network :public_network

  # If true, then any SSH connections made will enable agent forwarding.
  # Default value: false
  # config.ssh.forward_agent = true

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  config.vm.synced_folder ".", "/vagrant", type: "nfs", nfs_udp: false
  config.vm.network "private_network", ip: "192.168.255.2"
  
  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  config.vm.provider :virtualbox do |vb|
     vb.customize ["modifyvm", :id, "--memory", "1024"]
     # Enable Network Address Translation option to speed up external requests from VM
     vb.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
     vb.customize ["modifyvm", :id, "--natdnsproxy1", "on"]
  end

  # View the documentation for the provider you're using for more
  # information on available options.

  # Provision using bash script
  # first, fix the 'stdin: is not a tty' problem when trying to 'vagrant up'
  config.ssh.shell = "bash -c 'BASH_ENV=/etc/profile exec bash'"

  # args are environment variable name/value pairs (separated by whitespace) which will be created in the guest provisioning script and made available to the vagrant user
  # In this example, environment variables are being read from the host and passed to the guest
  # example: config.vm.provision :shell, :path => "vagrant-conf/vagrant-provision.sh", :args => "MY_VAR #{ENV['SOME_VALUE']} FOO #{ENV['BAR']}"

  # pass the environment variable 'BUILD_NUMBER' (used by both Jenkins and Team City) into the provisioning script
  config.vm.provision :shell, :path => "vagrant-conf/vagrant-provision.sh", :args => "BUILD_NUMBER #{ENV['BUILD_NUMBER']}"

end
