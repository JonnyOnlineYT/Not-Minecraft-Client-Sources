/*
здесь все спизжено
*/

#include <boost/beast/core.hpp>
#include <boost/beast/http.hpp>
#include <boost/beast/version.hpp>
#include <boost/asio.hpp>

namespace beast = boost::beast;         // from <boost/beast.hpp>
namespace http = beast::http;           // from <boost/beast/http.hpp>
namespace net = boost::asio;            // from <boost/asio.hpp>
using tcp = boost::asio::ip::tcp;       // from <boost/asio/ip/tcp.hpp>

#include "util.h"

class Connection : public std::enable_shared_from_this< Connection >
{
public:
    Connection( tcp::socket socket )
        : socket( std::move( socket ) )
    {

    }

    void Start( )
    {
        ReadRequest( );
        CheckDeadline( );
    }

private:
    tcp::socket socket;
    beast::flat_buffer buffer{ 8192 };
    http::request< http::dynamic_body > request;
    http::response< http::dynamic_body > response;
    net::steady_timer deadline{ socket.get_executor( ), std::chrono::seconds( 60 ) };

    void ReadRequest( )
    {
        auto self = shared_from_this( );

        http::async_read( socket, buffer, request,
                          [ self ]( beast::error_code ec, std::size_t bytes_transferred )
        {
            boost::ignore_unused( bytes_transferred );
            if( !ec )
                self->ProcessRequest( );
        } );
    }

    void ProcessRequest( )
    {
        response.version( request.version( ) );
        response.keep_alive( false );

        switch( request.method( ) )
        {
        case http::verb::get:
            response.result( http::status::ok );
            response.set( http::field::server, "die" );
            CreateResponse( );
            break;

        default:
            // We return responses indicating an error if
            // we do not recognize the request method.
            response.result( http::status::bad_request );
            response.set( http::field::content_type, "text/plain" );
            beast::ostream( response.body( ) )
                << "Invalid request-method '"
                << std::string( request.method_string( ) )
                << "'";
            break;
        }

        WriteResponse( );
    }

    void CreateResponse( )
    {
        /*if( request.target( ) == "/count" )
        {
            response.set( http::field::content_type, "text/html" );
            beast::ostream( response.body( ) )
                << "<html>\n"
                << "<title>eblan sdohni</title>\n"
                << "ok\n"
                << "</html>\n";
        }
        else if( request.target( ) == "/time" )
        {
            response.set( http::field::content_type, "text/html" );
            beast::ostream( response.body( ) )
                << "<html>\n"
                << "<title>eblan sdohni</title>\n"
                << "ok\n"
                << "</html>\n";
        }
        else
        {
            response.result( http::status::not_found );
            response.set( http::field::content_type, "text/plain" );
            beast::ostream( response.body( ) ) << "File not found\r\n";
        }*/

        std::string type = "";
        std::string str = util::MakeResponse( request.target( ).to_string( ), type );
        if( str.length( ) == 0 )
        {
            response.result( http::status::not_found );
            response.set( http::field::content_type, "text/plain" );
            beast::ostream( response.body( ) ) << "File not found\r\n";
            return;
        }

        response.set( http::field::content_type, type );
        response.result( http::status::ok );
        beast::ostream( response.body( ) ) << str;
    }

    void WriteResponse( )
    {
        auto self = shared_from_this( );

        response.content_length( response.body( ).size( ) );

        http::async_write( socket, response,
                           [ self ]( beast::error_code ec, std::size_t )
        {
            self->socket.shutdown( tcp::socket::shutdown_send, ec );
            self->deadline.cancel( );
        } );
    }

    void CheckDeadline( )
    {
        auto self = shared_from_this( );

        deadline.async_wait(
            [ self ]( beast::error_code ec )
        {
            if( !ec )
            {
                // Close socket to cancel any outstanding operation.
                self->socket.close( ec );
            }
        } );
    }
};

void AcceptConnections( tcp::acceptor& acceptor, tcp::socket& socket )
{
    acceptor.async_accept( socket, [ & ]( beast::error_code ec )
    {
        if( !ec )
            std::make_shared< Connection >( std::move( socket ) )->Start( );

        AcceptConnections( acceptor, socket );
    } );
}

void StartHTTPServer( )
{
    try
    {
        const auto address = net::ip::make_address( "0.0.0.0" );
        unsigned short port = static_cast< unsigned short >( 48888 );

        net::io_context ioc{ 1 };

        tcp::acceptor acceptor{ ioc, { address, port } };
        tcp::socket socket{ ioc };

        AcceptConnections( acceptor, socket );

        ioc.run( );
    }
    catch( const std::exception& e )
    {
        serverfail = true;
    }
}
